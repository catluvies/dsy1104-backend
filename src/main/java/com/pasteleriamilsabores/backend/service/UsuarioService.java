package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.UsuarioDTO;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Usuario;
import com.pasteleriamilsabores.backend.repository.BoletaRepository;
import com.pasteleriamilsabores.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BoletaRepository boletaRepository;

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> convertirADTO(usuario, false))
                .collect(Collectors.toList());
    }

    public UsuarioDTO obtenerUsuarioPorId(long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return convertirADTO(usuario, true);
    }

    public UsuarioDTO actualizarUsuario(long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Actualizar solo campos editables (no email, password, rut)
        if (usuarioDTO.getNombre() != null) {
            usuario.setNombre(usuarioDTO.getNombre());
        }
        if (usuarioDTO.getApellido() != null) {
            usuario.setApellido(usuarioDTO.getApellido());
        }
        if (usuarioDTO.getTelefono() != null) {
            usuario.setTelefono(usuarioDTO.getTelefono());
        }
        if (usuarioDTO.getDireccion() != null) {
            usuario.setDireccion(usuarioDTO.getDireccion());
        }
        if (usuarioDTO.getComuna() != null) {
            usuario.setComuna(usuarioDTO.getComuna());
        }
        if (usuarioDTO.getRegion() != null) {
            usuario.setRegion(usuarioDTO.getRegion());
        }
        if (usuarioDTO.getActivo() != null) {
            usuario.setActivo(usuarioDTO.getActivo());
        }
        if (usuarioDTO.getRol() != null) {
            usuario.setRol(usuarioDTO.getRol());
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return convertirADTO(actualizado, false);
    }

    private UsuarioDTO convertirADTO(Usuario usuario, boolean cargarHistorial) {
        int historialCompras = 0;
        if (cargarHistorial) {
            Long usuarioId = usuario.getId();
            if (usuarioId != null) {
                historialCompras = boletaRepository.countByUsuarioId(usuarioId);
            }
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setRut(usuario.getRut());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setComuna(usuario.getComuna());
        dto.setRegion(usuario.getRegion());
        dto.setActivo(usuario.getActivo());
        dto.setHistorialCompras(historialCompras);

        return dto;
    }
}
