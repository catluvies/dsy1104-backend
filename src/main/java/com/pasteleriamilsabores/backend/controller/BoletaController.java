package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.BoletaDTO;
import com.pasteleriamilsabores.backend.dto.CrearBoletaRequest;
import com.pasteleriamilsabores.backend.security.UsuarioPrincipal;
import com.pasteleriamilsabores.backend.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/boletas")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Boletas", description = "Gestión de boletas de compra")
public class BoletaController {

    private final BoletaService boletaService;

    @Operation(summary = "Listar todas las boletas", description = "Solo ADMIN y VENDEDOR")
    @GetMapping
    public ResponseEntity<List<BoletaDTO>> listarTodas() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @Operation(summary = "Obtener boleta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<BoletaDTO> obtenerPorId(@PathVariable long id) {
        return ResponseEntity.ok(boletaService.buscarPorId(id));
    }

    @Operation(summary = "Listar boletas de un usuario", description = "CLIENTE solo ve sus propias boletas")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<BoletaDTO>> listarPorUsuario(
            @PathVariable long usuarioId,
            Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        String rol = principal.getRol();

        if (rol.equals("ROLE_CLIENTE") && !principal.getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(boletaService.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Crear boleta", description = "Crea una nueva boleta con sus detalles")
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<BoletaDTO> crear(@PathVariable long usuarioId, @Valid @RequestBody CrearBoletaRequest request) {
        BoletaDTO creada = boletaService.crearBoleta(usuarioId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/boletas/{id}")
                .buildAndExpand(creada.getId()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @Operation(summary = "Actualizar estado de boleta", description = "Solo ADMIN")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<BoletaDTO> actualizarEstado(@PathVariable long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        BoletaDTO actualizada = boletaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar boleta", description = "Solo ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        boletaService.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }
}
