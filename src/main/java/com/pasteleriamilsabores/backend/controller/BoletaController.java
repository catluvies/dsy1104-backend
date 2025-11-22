package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.BoletaDTO;
import com.pasteleriamilsabores.backend.dto.CrearBoletaRequest;
import com.pasteleriamilsabores.backend.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin(origins = "http://localhost:5173")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    @GetMapping
    public ResponseEntity<List<BoletaDTO>> listarTodas() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(boletaService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<BoletaDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(boletaService.listarPorUsuario(usuarioId));
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<BoletaDTO> crear(@PathVariable Long usuarioId, @RequestBody CrearBoletaRequest request) {
        BoletaDTO creada = boletaService.crearBoleta(usuarioId, request);
        return ResponseEntity.created(URI.create("/api/boletas/" + creada.getId())).body(creada);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<BoletaDTO> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        BoletaDTO actualizada = boletaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletaService.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }
}
