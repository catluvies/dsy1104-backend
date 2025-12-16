package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.*;
import com.pasteleriamilsabores.backend.exception.BadRequestException;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Boleta;
import com.pasteleriamilsabores.backend.model.DetalleBoleta;
import com.pasteleriamilsabores.backend.model.Producto;
import com.pasteleriamilsabores.backend.model.ProductoVariante;
import com.pasteleriamilsabores.backend.model.Usuario;
import com.pasteleriamilsabores.backend.model.enums.EstadoBoleta;
import com.pasteleriamilsabores.backend.model.enums.TipoEntrega;
import com.pasteleriamilsabores.backend.model.enums.TipoNotificacion;
import com.pasteleriamilsabores.backend.repository.BoletaRepository;
import com.pasteleriamilsabores.backend.repository.ProductoRepository;
import com.pasteleriamilsabores.backend.repository.ProductoVarianteRepository;
import com.pasteleriamilsabores.backend.repository.UsuarioRepository;
import com.pasteleriamilsabores.backend.util.AppConstants;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoletaService {

    private final BoletaRepository boletaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProductoRepository productoRepository;

    private final ProductoVarianteRepository varianteRepository;

    private final FileStorageService fileStorageService;

    private final NotificacionService notificacionService;

    public List<BoletaDTO> listarTodas() {
        return boletaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<BoletaDTO> listarPorUsuario(long usuarioId) {
        return boletaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public BoletaDTO buscarPorId(long id) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta no encontrada"));
        return convertirADTO(boleta);
    }

    @Transactional
    public BoletaDTO crearBoleta(long usuarioId, CrearBoletaRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setEstado(EstadoBoleta.PENDIENTE);
        boleta.setMetodoPago(request.getMetodoPago());
        boleta.setTipoEntrega(request.getTipoEntrega());
        boleta.setHorarioEntrega(request.getHorarioEntrega());

        // Validar Comuna y Calcular Costo de Envío
        if (request.getTipoEntrega() == TipoEntrega.RETIRO) {
            // Caso: Retiro en Tienda
            boleta.setCostoEnvio(0.0);
            boleta.setComunaEntrega(AppConstants.COMUNA_RETIRO);
            boleta.setDireccionEntrega(AppConstants.DIRECCION_TIENDA);
        } else {
            // Caso: Despacho a Domicilio
            String comunaNormalizada = request.getComuna();
            if (!AppConstants.COSTOS_ENVIO.containsKey(comunaNormalizada)) {
                throw new BadRequestException("Comuna no válida o fuera de zona de reparto: " + comunaNormalizada);
            }
            Double costoCalculado = AppConstants.COSTOS_ENVIO.get(comunaNormalizada);
            boleta.setCostoEnvio(costoCalculado);
            boleta.setComunaEntrega(comunaNormalizada);
            boleta.setDireccionEntrega(request.getDireccionEntrega());
        }

        boleta.setRegionEntrega(AppConstants.REGION_OPERACION);
        boleta.setNotas(request.getNotasAdicionales());

        // Validar Fecha de Entrega (Agendamiento Obligatorio)
        if (request.getFechaEntrega() == null) {
            throw new BadRequestException("La fecha y hora de entrega son obligatorias");
        }
        if (request.getFechaEntrega().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("La fecha de entrega no puede ser en el pasado");
        }
        boleta.setFechaEntrega(request.getFechaEntrega());

        double subtotal = 0.0;
        List<DetalleBoleta> detalles = new ArrayList<>();

        for (DetalleBoletaRequest detalleRequest : request.getDetalles()) {
            if (detalleRequest.getProductoId() == null) {
                throw new BadRequestException("El ID del producto es requerido");
            }
            long productoId = detalleRequest.getProductoId();

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            ProductoVariante variante = null;
            Double precioUnitario;
            Integer stockDisponible;
            String nombreItem;

            // Si se especifica una variante, usarla
            if (detalleRequest.getVarianteId() != null) {
                long varianteId = detalleRequest.getVarianteId();
                variante = varianteRepository.findById(varianteId)
                        .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada"));

                // Validar que la variante pertenezca al producto
                if (!variante.getProducto().getId().equals(productoId)) {
                    throw new BadRequestException("La variante no pertenece al producto especificado");
                }

                // Validar que la variante esté activa
                if (!variante.getActivo()) {
                    throw new BadRequestException("La variante seleccionada no está disponible");
                }

                precioUnitario = variante.getPrecio();
                stockDisponible = variante.getStock();
                nombreItem = producto.getNombre() + " - " + variante.getNombre();
            } else {
                // Sin variante: usar producto base
                precioUnitario = producto.getPrecio();
                stockDisponible = producto.getStock();
                nombreItem = producto.getNombre();
            }

            // Validar stock
            if (stockDisponible < detalleRequest.getCantidad()) {
                throw new BadRequestException("Stock insuficiente para: " + nombreItem);
            }

            // Descontar stock
            if (variante != null) {
                variante.setStock(variante.getStock() - detalleRequest.getCantidad());
                varianteRepository.save(variante);
            } else {
                producto.setStock(producto.getStock() - detalleRequest.getCantidad());
                productoRepository.save(producto);
            }

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setVariante(variante);
            detalle.setCantidad(detalleRequest.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(detalleRequest.getCantidad() * precioUnitario);

            subtotal += detalle.getSubtotal();
            detalles.add(detalle);
        }

        // Calcular Fecha de Expiración
        boleta.setFechaExpiracion(LocalDateTime.now().plusMinutes(AppConstants.TIEMPO_PAGO_MINUTOS));

        boleta.setSubtotal(subtotal);
        boleta.setTotal(subtotal + boleta.getCostoEnvio());
        boleta.setDetalles(detalles);

        Boleta guardada = boletaRepository.save(boleta);
        return convertirADTO(guardada);
    }

    @Transactional
    public BoletaDTO crearBoletaConComprobante(long usuarioId, CrearBoletaRequest request, MultipartFile comprobante) {
        BoletaDTO boletaCreada = crearBoleta(usuarioId, request);

        if (comprobante != null && !comprobante.isEmpty()) {
            long boletaId = boletaCreada.getId();
            Boleta boleta = boletaRepository.findById(boletaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Boleta no encontrada"));

            String fileName = fileStorageService.storeFile(comprobante);
            String fileUrl = "/uploads/" + fileName;
            boleta.setComprobanteUrl(fileUrl);
            boleta.setFechaComprobante(LocalDateTime.now());

            Boleta actualizada = boletaRepository.save(boleta);
            return convertirADTO(actualizada);
        }

        return boletaCreada;
    }

    @Transactional
    public BoletaDTO actualizarEstado(long id, EstadoBoleta nuevoEstado) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta no encontrada"));

        EstadoBoleta estadoAnterior = boleta.getEstado();
        boleta.setEstado(nuevoEstado);
        Boleta actualizada = boletaRepository.save(boleta);

        // Crear notificación para el cliente
        if (estadoAnterior != nuevoEstado) {
            String titulo = obtenerTituloNotificacion(nuevoEstado);
            String mensaje = obtenerMensajeNotificacion(nuevoEstado, boleta.getId());
            TipoNotificacion tipo = obtenerTipoNotificacion(nuevoEstado);
            notificacionService.crearNotificacion(
                    boleta.getUsuario().getId(),
                    titulo,
                    mensaje,
                    tipo,
                    boleta.getId()
            );
        }

        return convertirADTO(actualizada);
    }

    private String obtenerTituloNotificacion(EstadoBoleta estado) {
        return switch (estado) {
            case CONFIRMADA -> "Pedido confirmado";
            case PREPARANDO -> "Pedido en preparación";
            case LISTA -> "Pedido listo";
            case ENTREGADA -> "Pedido entregado";
            case CANCELADA -> "Pedido cancelado";
            default -> "Actualización de pedido";
        };
    }

    private String obtenerMensajeNotificacion(EstadoBoleta estado, Long boletaId) {
        return switch (estado) {
            case CONFIRMADA -> "Tu pedido #" + boletaId + " ha sido confirmado. Pronto comenzaremos a prepararlo.";
            case PREPARANDO -> "Tu pedido #" + boletaId + " está siendo preparado con mucho cariño.";
            case LISTA -> "Tu pedido #" + boletaId + " está listo para ser entregado/retirado.";
            case ENTREGADA -> "Tu pedido #" + boletaId + " ha sido entregado. ¡Gracias por tu compra!";
            case CANCELADA -> "Tu pedido #" + boletaId + " ha sido cancelado.";
            default -> "El estado de tu pedido #" + boletaId + " ha sido actualizado.";
        };
    }

    private TipoNotificacion obtenerTipoNotificacion(EstadoBoleta estado) {
        return switch (estado) {
            case CONFIRMADA, PREPARANDO, LISTA, ENTREGADA -> TipoNotificacion.EXITO;
            case CANCELADA -> TipoNotificacion.ADVERTENCIA;
            default -> TipoNotificacion.INFO;
        };
    }

    public void eliminarBoleta(long id) {
        if (!boletaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Boleta no encontrada");
        }
        boletaRepository.deleteById(id);
    }

    @Transactional
    public BoletaDTO subirComprobante(long boletaId, long usuarioId, MultipartFile archivo) {
        Boleta boleta = boletaRepository.findById(boletaId)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta no encontrada"));

        // Validar que la boleta pertenezca al usuario
        if (!boleta.getUsuario().getId().equals(usuarioId)) {
            throw new BadRequestException("No tienes permiso para subir comprobante a esta boleta");
        }

        // Validar que el estado sea PENDIENTE
        if (boleta.getEstado() != EstadoBoleta.PENDIENTE) {
            throw new BadRequestException("Solo se puede subir comprobante a boletas en estado PENDIENTE");
        }

        // Validar que el método de pago sea TRANSFERENCIA
        if (boleta.getMetodoPago() != com.pasteleriamilsabores.backend.model.enums.MetodoPago.TRANSFERENCIA) {
            throw new BadRequestException("Solo se requiere comprobante para pagos por transferencia");
        }

        // Guardar el archivo
        String fileName = fileStorageService.storeFile(archivo);
        String fileUrl = "/uploads/" + fileName;

        // Actualizar la boleta
        boleta.setComprobanteUrl(fileUrl);
        boleta.setFechaComprobante(LocalDateTime.now());

        Boleta actualizada = boletaRepository.save(boleta);
        return convertirADTO(actualizada);
    }

    private BoletaDTO convertirADTO(Boleta boleta) {
        List<DetalleBoletaDTO> detallesDTO = boleta.getDetalles().stream()
                .map(detalle -> {
                    DetalleBoletaDTO dto = new DetalleBoletaDTO();
                    dto.setId(detalle.getId());
                    dto.setProductoId(detalle.getProducto().getId());
                    dto.setProductoNombre(detalle.getProducto().getNombre());
                    dto.setCantidad(detalle.getCantidad());
                    dto.setPrecioUnitario(detalle.getPrecioUnitario());
                    dto.setSubtotal(detalle.getSubtotal());

                    // Incluir info de variante si existe
                    if (detalle.getVariante() != null) {
                        dto.setVarianteId(detalle.getVariante().getId());
                        dto.setVarianteNombre(detalle.getVariante().getNombre());
                        dto.setVariantePorciones(detalle.getVariante().getPorciones());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        BoletaDTO dto = new BoletaDTO();
        dto.setId(boleta.getId());
        dto.setUsuarioId(boleta.getUsuario().getId());
        dto.setUsuarioNombre(boleta.getUsuario().getNombre() + " " + boleta.getUsuario().getApellido());
        dto.setFechaCreacion(boleta.getFechaCreacion());
        dto.setTotal(boleta.getTotal());
        dto.setSubtotal(boleta.getSubtotal());
        dto.setCostoEnvio(boleta.getCostoEnvio());
        dto.setTipoEntrega(boleta.getTipoEntrega());
        dto.setHorarioEntrega(boleta.getHorarioEntrega());
        dto.setMetodoPago(boleta.getMetodoPago());
        dto.setDireccionEntrega(boleta.getDireccionEntrega());
        dto.setComunaEntrega(boleta.getComunaEntrega());
        dto.setRegionEntrega(boleta.getRegionEntrega());
        dto.setNotas(boleta.getNotas());
        dto.setFechaEntrega(boleta.getFechaEntrega());
        dto.setEstado(boleta.getEstado());
        dto.setDetalles(detallesDTO);
        dto.setComprobanteUrl(boleta.getComprobanteUrl());
        dto.setFechaComprobante(boleta.getFechaComprobante());
        return dto;
    }
}
