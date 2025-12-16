package com.pasteleriamilsabores.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleriamilsabores.backend.dto.CategoriaDTO;
import com.pasteleriamilsabores.backend.service.CategoriaService;
import com.pasteleriamilsabores.backend.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @Operation(summary = "Listar categorías activas")
    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaDTO>> listarActivas() {
        return ResponseEntity.ok(categoriaService.listarActivas());
    }

    @Operation(summary = "Obtener categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerPorId(@PathVariable long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @Operation(summary = "Crear categoría", description = "Solo ADMIN. Permite subir imagen")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoriaDTO> crear(
            @RequestPart("categoria") String categoriaJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws Exception {

        CategoriaDTO categoriaDTO = objectMapper.readValue(categoriaJson, CategoriaDTO.class);

        if (imagen != null && !imagen.isEmpty()) {
            String fileName = fileStorageService.storeFile(imagen);
            String fileUrl = "/uploads/" + fileName;
            categoriaDTO.setImagenUrl(fileUrl);
        }

        CategoriaDTO creada = categoriaService.crearCategoria(categoriaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/categorias/{id}")
                .buildAndExpand(creada.getId()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @Operation(summary = "Actualizar categoría", description = "Solo ADMIN. Permite cambiar imagen")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoriaDTO> actualizar(
            @PathVariable long id,
            @RequestPart("categoria") String categoriaJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws Exception {

        CategoriaDTO categoriaDTO = objectMapper.readValue(categoriaJson, CategoriaDTO.class);

        if (imagen != null && !imagen.isEmpty()) {
            String fileName = fileStorageService.storeFile(imagen);
            String fileUrl = "/uploads/" + fileName;
            categoriaDTO.setImagenUrl(fileUrl);
        }

        CategoriaDTO actualizada = categoriaService.actualizarCategoria(id, categoriaDTO);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar categoría", description = "Solo ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
