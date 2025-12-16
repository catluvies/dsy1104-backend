package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.ProductoVarianteDTO;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Producto;
import com.pasteleriamilsabores.backend.model.ProductoVariante;
import com.pasteleriamilsabores.backend.repository.ProductoRepository;
import com.pasteleriamilsabores.backend.repository.ProductoVarianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoVarianteService {

    private final ProductoVarianteRepository varianteRepository;
    private final ProductoRepository productoRepository;

    public List<ProductoVarianteDTO> listarPorProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        return varianteRepository.findByProductoId(productoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoVarianteDTO> listarActivasPorProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        return varianteRepository.findByProductoIdAndActivoTrue(productoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoVarianteDTO buscarPorId(Long id) {
        ProductoVariante variante = varianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada"));
        return convertirADTO(variante);
    }

    public ProductoVarianteDTO crearVariante(Long productoId, ProductoVarianteDTO dto) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        ProductoVariante variante = new ProductoVariante();
        variante.setProducto(producto);
        variante.setNombre(dto.getNombre());
        variante.setPorciones(dto.getPorciones());
        variante.setPrecio(dto.getPrecio());
        variante.setStock(dto.getStock());
        variante.setActivo(true);

        ProductoVariante guardada = varianteRepository.save(variante);
        return convertirADTO(guardada);
    }

    public ProductoVarianteDTO actualizarVariante(Long id, ProductoVarianteDTO dto) {
        ProductoVariante variante = varianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada"));

        variante.setNombre(dto.getNombre());
        variante.setPorciones(dto.getPorciones());
        variante.setPrecio(dto.getPrecio());
        variante.setStock(dto.getStock());
        if (dto.getActivo() != null) {
            variante.setActivo(dto.getActivo());
        }

        ProductoVariante actualizada = varianteRepository.save(variante);
        return convertirADTO(actualizada);
    }

    public void eliminarVariante(Long id) {
        if (!varianteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Variante no encontrada");
        }
        varianteRepository.deleteById(id);
    }

    private ProductoVarianteDTO convertirADTO(ProductoVariante variante) {
        ProductoVarianteDTO dto = new ProductoVarianteDTO();
        dto.setId(variante.getId());
        dto.setProductoId(variante.getProducto().getId());
        dto.setNombre(variante.getNombre());
        dto.setPorciones(variante.getPorciones());
        dto.setPrecio(variante.getPrecio());
        dto.setStock(variante.getStock());
        dto.setActivo(variante.getActivo());
        return dto;
    }
}
