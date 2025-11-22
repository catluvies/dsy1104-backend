package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.ProductoDTO;
import com.pasteleriamilsabores.backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoDTO>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoriaId));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO creado = productoService.crearProducto(productoDTO);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        ProductoDTO actualizado = productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
