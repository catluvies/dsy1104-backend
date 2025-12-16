package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.ProductoDTO;
import com.pasteleriamilsabores.backend.dto.ProductoVarianteDTO;
import com.pasteleriamilsabores.backend.exception.BadRequestException;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Categoria;
import com.pasteleriamilsabores.backend.model.Producto;
import com.pasteleriamilsabores.backend.model.ProductoVariante;
import com.pasteleriamilsabores.backend.repository.CategoriaRepository;
import com.pasteleriamilsabores.backend.repository.ProductoRepository;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    private final CategoriaRepository categoriaRepository;

    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarActivos() {
        // Solo productos activos cuya categoría también esté activa
        return productoRepository.findByActivoTrueAndCategoriaActivaTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarPorCategoria(long categoriaId) {
        // Solo productos activos de la categoría
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO buscarPorId(long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        return convertirADTO(producto);
    }

    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        if (productoDTO.getCategoriaId() == null) {
            throw new BadRequestException("El ID de la categoría es obligatorio");
        }
        long categoriaId = productoDTO.getCategoriaId();
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setSku(productoDTO.getSku());
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setImagenUrl(productoDTO.getImagenUrl());
        producto.setCategoria(categoria);
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setIngredientes(productoDTO.getIngredientes());
        producto.setCantidadMedida(productoDTO.getCantidadMedida());
        producto.setUnidadMedida(productoDTO.getUnidadMedida());
        producto.setPorciones(productoDTO.getPorciones());
        producto.setDuracionDias(productoDTO.getDuracionDias());
        producto.setCondicionConservacion(productoDTO.getCondicionConservacion());
        producto.setAlergenos(productoDTO.getAlergenos());
        producto.setActivo(true);

        Producto guardado = productoRepository.save(producto);
        return convertirADTO(guardado);
    }

    public ProductoDTO actualizarProducto(long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (productoDTO.getCategoriaId() == null) {
            throw new BadRequestException("El ID de la categoría es obligatorio");
        }
        long categoriaId = productoDTO.getCategoriaId();
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        producto.setSku(productoDTO.getSku());
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        // Solo actualizar imagen si se envía una nueva (preservar existente si es null)
        if (productoDTO.getImagenUrl() != null) {
            producto.setImagenUrl(productoDTO.getImagenUrl());
        }
        producto.setCategoria(categoria);
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setIngredientes(productoDTO.getIngredientes());
        producto.setCantidadMedida(productoDTO.getCantidadMedida());
        producto.setUnidadMedida(productoDTO.getUnidadMedida());
        producto.setPorciones(productoDTO.getPorciones());
        producto.setDuracionDias(productoDTO.getDuracionDias());
        producto.setCondicionConservacion(productoDTO.getCondicionConservacion());
        producto.setAlergenos(productoDTO.getAlergenos());
        producto.setActivo(productoDTO.getActivo());

        Producto actualizado = productoRepository.save(producto);
        return convertirADTO(actualizado);
    }

    public void eliminarProducto(long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    private ProductoDTO convertirADTO(Producto producto) {
        List<ProductoVarianteDTO> variantesDTO = producto.getVariantes() != null
                ? producto.getVariantes().stream()
                        .filter(ProductoVariante::getActivo)
                        .map(this::convertirVarianteADTO)
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return new ProductoDTO(
                producto.getId(),
                producto.getSku(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getImagenUrl(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getIngredientes(),
                producto.getCantidadMedida(),
                producto.getUnidadMedida(),
                producto.getPorciones(),
                producto.getDuracionDias(),
                producto.getCondicionConservacion(),
                producto.getAlergenos(),
                producto.getActivo(),
                variantesDTO);
    }

    private ProductoVarianteDTO convertirVarianteADTO(ProductoVariante variante) {
        return new ProductoVarianteDTO(
                variante.getId(),
                variante.getProducto().getId(),
                variante.getCantidad(),
                variante.getUnidadMedida(),
                variante.getNombreDisplay(),
                variante.getPrecio(),
                variante.getStock(),
                variante.getActivo());
    }
}
