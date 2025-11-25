package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.ProductoDTO;
import com.pasteleriamilsabores.backend.service.FileStorageService;
import com.pasteleriamilsabores.backend.service.ProductoService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoDTO>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable long categoriaId) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoriaId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> crear(
            @RequestPart("producto") ProductoDTO productoDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        if (imagen != null && !imagen.isEmpty()) {
            String fileName = fileStorageService.storeFile(imagen);
            // Construir URL relativa (o absoluta si prefieres)
            // Para simplificar y que funcione en cualquier host, guardamos la ruta relativa
            // El frontend deberá anteponer el dominio base
            String fileUrl = "/uploads/" + fileName;
            productoDTO.setImagenUrl(fileUrl);
        }

        ProductoDTO creado = productoService.crearProducto(productoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/productos/{id}")
                .buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable long id,
            @RequestPart("producto") ProductoDTO productoDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        if (imagen != null && !imagen.isEmpty()) {
            String fileName = fileStorageService.storeFile(imagen);
            String fileUrl = "/uploads/" + fileName;
            productoDTO.setImagenUrl(fileUrl);
        }

        ProductoDTO actualizado = productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
