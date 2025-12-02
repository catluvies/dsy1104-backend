package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.BoletaDTO;
import com.pasteleriamilsabores.backend.dto.CrearBoletaRequest;
import com.pasteleriamilsabores.backend.model.enums.EstadoBoleta;
import com.pasteleriamilsabores.backend.model.enums.RolUsuario;
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
@RequiredArgsConstructor
@Tag(name = "Boletas", description = "Gestión de boletas de compra")
public class BoletaController {

    private final BoletaService boletaService;

    @Operation(summary = "Listar todas las boletas", description = "Solo ADMIN y VENDEDOR")
    @GetMapping
    public ResponseEntity<List<BoletaDTO>> listarTodas() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @Operation(summary = "Obtener boleta por ID", description = "CLIENTE solo puede ver sus propias boletas")
    @GetMapping("/{id}")
    public ResponseEntity<BoletaDTO> obtenerPorId(@PathVariable long id, Authentication authentication) {
        BoletaDTO boleta = boletaService.buscarPorId(id);
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();

        if (principal.getRol().equals(RolUsuario.ROLE_CLIENTE.name())
                && !principal.getId().equals(boleta.getUsuarioId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(boleta);
    }

    @Operation(summary = "Listar boletas de un usuario", description = "CLIENTE solo ve sus propias boletas")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<BoletaDTO>> listarPorUsuario(
            @PathVariable long usuarioId,
            Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        String rol = principal.getRol();

        if (rol.equals(RolUsuario.ROLE_CLIENTE.name()) && !principal.getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(boletaService.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Crear boleta", description = "CLIENTE solo puede crear boletas para sí mismo")
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<BoletaDTO> crear(
            @PathVariable long usuarioId,
            @Valid @RequestBody CrearBoletaRequest request,
            Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();

        if (principal.getRol().equals(RolUsuario.ROLE_CLIENTE.name()) && !principal.getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BoletaDTO creada = boletaService.crearBoleta(usuarioId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/boletas/{id}")
                .buildAndExpand(creada.getId()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @Operation(summary = "Actualizar estado de boleta", description = "Solo ADMIN")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<BoletaDTO> actualizarEstado(@PathVariable long id, @RequestBody Map<String, String> body) {
        String nuevoEstadoStr = body.get("estado");
        EstadoBoleta nuevoEstado = EstadoBoleta.valueOf(nuevoEstadoStr);
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
