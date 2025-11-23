package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.BoletaDTO;
import com.pasteleriamilsabores.backend.dto.CrearBoletaRequest;
import com.pasteleriamilsabores.backend.service.BoletaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaService boletaService;

    @GetMapping
    public ResponseEntity<List<BoletaDTO>> listarTodas() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletaDTO> obtenerPorId(@PathVariable long id) {
        return ResponseEntity.ok(boletaService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<BoletaDTO>> listarPorUsuario(@PathVariable long usuarioId) {
        return ResponseEntity.ok(boletaService.listarPorUsuario(usuarioId));
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<BoletaDTO> crear(@PathVariable long usuarioId, @RequestBody CrearBoletaRequest request) {
        BoletaDTO creada = boletaService.crearBoleta(usuarioId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/boletas/{id}")
                .buildAndExpand(creada.getId()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<BoletaDTO> actualizarEstado(@PathVariable long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        BoletaDTO actualizada = boletaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        boletaService.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }
}
