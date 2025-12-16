package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.ProductoVarianteDTO;
import com.pasteleriamilsabores.backend.service.ProductoVarianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Variantes de Producto", description = "Gestión de tamaños/variantes de productos")
public class ProductoVarianteController {

    private final ProductoVarianteService varianteService;

    @Operation(summary = "Listar variantes de un producto")
    @GetMapping("/productos/{productoId}/variantes")
    public ResponseEntity<List<ProductoVarianteDTO>> listarPorProducto(@PathVariable long productoId) {
        return ResponseEntity.ok(varianteService.listarPorProducto(productoId));
    }

    @Operation(summary = "Listar variantes activas de un producto")
    @GetMapping("/productos/{productoId}/variantes/activas")
    public ResponseEntity<List<ProductoVarianteDTO>> listarActivasPorProducto(@PathVariable long productoId) {
        return ResponseEntity.ok(varianteService.listarActivasPorProducto(productoId));
    }

    @Operation(summary = "Obtener variante por ID")
    @GetMapping("/variantes/{id}")
    public ResponseEntity<ProductoVarianteDTO> obtenerPorId(@PathVariable long id) {
        return ResponseEntity.ok(varianteService.buscarPorId(id));
    }

    @Operation(summary = "Crear variante de producto", description = "Solo ADMIN")
    @PostMapping("/productos/{productoId}/variantes")
    public ResponseEntity<ProductoVarianteDTO> crear(
            @PathVariable long productoId,
            @Valid @RequestBody ProductoVarianteDTO varianteDTO) {
        ProductoVarianteDTO creada = varianteService.crearVariante(productoId, varianteDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/variantes/{id}")
                .buildAndExpand(creada.getId())
                .toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @Operation(summary = "Actualizar variante", description = "Solo ADMIN")
    @PutMapping("/variantes/{id}")
    public ResponseEntity<ProductoVarianteDTO> actualizar(
            @PathVariable long id,
            @Valid @RequestBody ProductoVarianteDTO varianteDTO) {
        return ResponseEntity.ok(varianteService.actualizarVariante(id, varianteDTO));
    }

    @Operation(summary = "Eliminar variante", description = "Solo ADMIN")
    @DeleteMapping("/variantes/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        varianteService.eliminarVariante(id);
        return ResponseEntity.noContent().build();
    }
}
