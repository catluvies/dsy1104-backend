package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.UsuarioDTO;
import com.pasteleriamilsabores.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios", description = "Solo ADMIN")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @Operation(summary = "Obtener usuario por ID", description = "ADMIN o el propio usuario")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @Operation(summary = "Actualizar usuario", description = "Solo ADMIN puede cambiar rol y activo")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable long id,
            @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuarioDTO));
    }
}
