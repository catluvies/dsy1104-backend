package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.ProductoDTO;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Categoria;
import com.pasteleriamilsabores.backend.model.Producto;
import com.pasteleriamilsabores.backend.repository.CategoriaRepository;
import com.pasteleriamilsabores.backend.repository.ProductoRepository;

import org.springframework.stereotype.Service;

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
        return productoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarPorCategoria(long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
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
            throw new ResourceNotFoundException("El ID de la categoría es obligatorio");
        }
        long categoriaId = productoDTO.getCategoriaId();
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setImagenUrl(productoDTO.getImagenUrl());
        producto.setCategoria(categoria);
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setIngredientes(productoDTO.getIngredientes());
        producto.setFormatoVenta(productoDTO.getFormatoVenta());
        producto.setPorciones(productoDTO.getPorciones());
        producto.setPeso(productoDTO.getPeso());
        producto.setDuracion(productoDTO.getDuracion());
        producto.setTiempoPreparacion(productoDTO.getTiempoPreparacion());
        producto.setOpcionPersonalizacion(productoDTO.getOpcionPersonalizacion());
        producto.setNotas(productoDTO.getNotas());
        producto.setActivo(true);

        Producto guardado = productoRepository.save(producto);
        return convertirADTO(guardado);
    }

    public ProductoDTO actualizarProducto(long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (productoDTO.getCategoriaId() == null) {
            throw new ResourceNotFoundException("El ID de la categoría es obligatorio");
        }
        long categoriaId = productoDTO.getCategoriaId();
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setImagenUrl(productoDTO.getImagenUrl());
        producto.setCategoria(categoria);
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setIngredientes(productoDTO.getIngredientes());
        producto.setFormatoVenta(productoDTO.getFormatoVenta());
        producto.setPorciones(productoDTO.getPorciones());
        producto.setPeso(productoDTO.getPeso());
        producto.setDuracion(productoDTO.getDuracion());
        producto.setTiempoPreparacion(productoDTO.getTiempoPreparacion());
        producto.setOpcionPersonalizacion(productoDTO.getOpcionPersonalizacion());
        producto.setNotas(productoDTO.getNotas());
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
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getImagenUrl(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getIngredientes(),
                producto.getFormatoVenta(),
                producto.getPorciones(),
                producto.getPeso(),
                producto.getDuracion(),
                producto.getTiempoPreparacion(),
                producto.getOpcionPersonalizacion(),
                producto.getNotas(),
                producto.getActivo());
    }
}
