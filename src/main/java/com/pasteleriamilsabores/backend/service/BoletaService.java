package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.*;
import com.pasteleriamilsabores.backend.exception.BadRequestException;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Boleta;
import com.pasteleriamilsabores.backend.model.DetalleBoleta;
import com.pasteleriamilsabores.backend.model.Producto;
import com.pasteleriamilsabores.backend.model.Usuario;
import com.pasteleriamilsabores.backend.model.enums.EstadoBoleta;
import com.pasteleriamilsabores.backend.model.enums.TipoEntrega;
import com.pasteleriamilsabores.backend.repository.BoletaRepository;
import com.pasteleriamilsabores.backend.repository.ProductoRepository;
import com.pasteleriamilsabores.backend.repository.UsuarioRepository;
import com.pasteleriamilsabores.backend.util.AppConstants;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoletaService {

    private final BoletaRepository boletaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProductoRepository productoRepository;

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
        if (request.getFechaEntrega().isBefore(java.time.LocalDateTime.now())) {
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

            if (producto.getStock() < detalleRequest.getCantidad()) {
                throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalleRequest.getCantidad());
            productoRepository.save(producto);

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleRequest.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(detalleRequest.getCantidad() * producto.getPrecio());

            subtotal += detalle.getSubtotal();
            detalles.add(detalle);
        }

        // Calcular Fecha de Expiración (45 minutos fijo)
        boleta.setFechaExpiracion(java.time.LocalDateTime.now().plusMinutes(45));

        boleta.setSubtotal(subtotal);
        boleta.setTotal(subtotal + boleta.getCostoEnvio());
        boleta.setDetalles(detalles);

        Boleta guardada = boletaRepository.save(boleta);
        return convertirADTO(guardada);
    }

    public BoletaDTO actualizarEstado(long id, EstadoBoleta nuevoEstado) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta no encontrada"));

        boleta.setEstado(nuevoEstado);
        Boleta actualizada = boletaRepository.save(boleta);
        return convertirADTO(actualizada);
    }

    public void eliminarBoleta(long id) {
        if (!boletaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Boleta no encontrada");
        }
        boletaRepository.deleteById(id);
    }

    private BoletaDTO convertirADTO(Boleta boleta) {
        List<DetalleBoletaDTO> detallesDTO = boleta.getDetalles().stream()
                .map(detalle -> new DetalleBoletaDTO(
                        detalle.getId(),
                        detalle.getProducto().getId(),
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getSubtotal()))
                .collect(Collectors.toList());

        return new BoletaDTO(
                boleta.getId(),
                boleta.getUsuario().getId(),
                boleta.getUsuario().getNombre() + " " + boleta.getUsuario().getApellido(),
                boleta.getFechaCreacion(),
                boleta.getTotal(),
                boleta.getSubtotal(),
                boleta.getCostoEnvio(),
                boleta.getTipoEntrega(),
                boleta.getHorarioEntrega(),
                boleta.getMetodoPago(),
                boleta.getDireccionEntrega(),
                boleta.getComunaEntrega(),
                boleta.getRegionEntrega(),
                boleta.getNotas(),
                boleta.getFechaEntrega(),
                boleta.getEstado(),
                detallesDTO);
    }
}
