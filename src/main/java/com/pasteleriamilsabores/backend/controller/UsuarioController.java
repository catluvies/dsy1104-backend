package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.UsuarioDTO;
import com.pasteleriamilsabores.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
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

    @Operation(summary = "Actualizar perfil propio", description = "Usuario edita sus datos personales")
    @PutMapping("/{id}/perfil")
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<UsuarioDTO> actualizarPerfil(
            @PathVariable long id,
            @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(id, usuarioDTO));
    }

    @Operation(summary = "Actualizar usuario (Admin)", description = "ADMIN puede cambiar rol y activo")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarUsuarioAdmin(
            @PathVariable long id,
            @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizarUsuarioAdmin(id, usuarioDTO));
    }

    @Operation(summary = "Eliminar usuario", description = "Solo ADMIN")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Crear vendedor", description = "Solo ADMIN")
    @PostMapping("/vendedor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> crearVendedor(
            @Valid @RequestBody com.pasteleriamilsabores.backend.dto.CrearVendedorRequest request) {
        UsuarioDTO nuevoVendedor = usuarioService.crearVendedor(request);
        java.net.URI location = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoVendedor.getId())
                .toUri();
        return ResponseEntity.created(location).body(nuevoVendedor);
    }
}
