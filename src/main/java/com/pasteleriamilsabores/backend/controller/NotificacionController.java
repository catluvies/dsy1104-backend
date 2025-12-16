package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.NotificacionDTO;
import com.pasteleriamilsabores.backend.security.UsuarioPrincipal;
import com.pasteleriamilsabores.backend.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión de notificaciones de usuario")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Operation(summary = "Listar notificaciones del usuario", description = "Obtiene todas las notificaciones del usuario autenticado")
    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> listarMisNotificaciones(Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(notificacionService.listarPorUsuario(principal.getId()));
    }

    @Operation(summary = "Listar notificaciones no leídas", description = "Obtiene las notificaciones no leídas del usuario")
    @GetMapping("/no-leidas")
    public ResponseEntity<List<NotificacionDTO>> listarNoLeidas(Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(notificacionService.listarNoLeidasPorUsuario(principal.getId()));
    }

    @Operation(summary = "Contar notificaciones no leídas", description = "Obtiene el número de notificaciones sin leer")
    @GetMapping("/contador")
    public ResponseEntity<Map<String, Long>> contarNoLeidas(Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        long contador = notificacionService.contarNoLeidas(principal.getId());
        return ResponseEntity.ok(Map.of("noLeidas", contador));
    }

    @Operation(summary = "Marcar notificación como leída", description = "Marca una notificación específica como leída")
    @PatchMapping("/{id}/leer")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @Operation(summary = "Marcar todas como leídas", description = "Marca todas las notificaciones del usuario como leídas")
    @PatchMapping("/leer-todas")
    public ResponseEntity<Void> marcarTodasComoLeidas(Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        notificacionService.marcarTodasComoLeidas(principal.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación específica")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
