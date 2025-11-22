package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.CategoriaDTO;
import com.pasteleriamilsabores.backend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaDTO>> listarActivas() {
        return ResponseEntity.ok(categoriaService.listarActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO creada = categoriaService.crearCategoria(categoriaDTO);
        return ResponseEntity.created(URI.create("/api/categorias/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO actualizada = categoriaService.actualizarCategoria(id, categoriaDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
