package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.*;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Boleta;
import com.pasteleriamilsabores.backend.model.DetalleBoleta;
import com.pasteleriamilsabores.backend.model.Producto;
import com.pasteleriamilsabores.backend.model.Usuario;
import com.pasteleriamilsabores.backend.repository.BoletaRepository;
import com.pasteleriamilsabores.backend.repository.ProductoRepository;
import com.pasteleriamilsabores.backend.repository.UsuarioRepository;

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
        boleta.setEstado("PENDIENTE");

        double total = 0.0;
        List<DetalleBoleta> detalles = new ArrayList<>();

        for (DetalleBoletaRequest detalleRequest : request.getDetalles()) {
            if (detalleRequest.getProductoId() == null) {
                throw new IllegalArgumentException("El ID del producto es requerido");
            }
            long productoId = detalleRequest.getProductoId();

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleRequest.getCantidad());
            detalle.setPrecioUnitario(detalleRequest.getPrecioUnitario());
            detalle.setSubtotal(detalleRequest.getCantidad() * detalleRequest.getPrecioUnitario());
            detalle.setTamaño(detalleRequest.getTamaño());

            total += detalle.getSubtotal();
            detalles.add(detalle);
        }

        boleta.setTotal(total);
        boleta.setDetalles(detalles);

        Boleta guardada = boletaRepository.save(boleta);
        return convertirADTO(guardada);
    }

    public BoletaDTO actualizarEstado(long id, String nuevoEstado) {
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
                        detalle.getSubtotal(),
                        detalle.getTamaño()))
                .collect(Collectors.toList());

        return new BoletaDTO(
                boleta.getId(),
                boleta.getUsuario().getId(),
                boleta.getUsuario().getNombre(),
                boleta.getFecha(),
                boleta.getTotal(),
                boleta.getEstado(),
                detallesDTO);
    }
}
