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
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID es requerido");
        }
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return convertirADTO(usuario);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        int historialCompras = 0;
        Long usuarioId = usuario.getId();
        if (usuarioId != null) {
            historialCompras = boletaRepository.countByUsuarioId(usuarioId);
        }

        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .rut(usuario.getRut())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .comuna(usuario.getComuna())
                .ultimaConexion(usuario.getUltimaConexion())
                .historialCompras(historialCompras)
                .build();
    }
}
