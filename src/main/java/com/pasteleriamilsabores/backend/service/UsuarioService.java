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

    public UsuarioDTO actualizarPerfil(long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setDireccion(usuarioDTO.getDireccion());
        usuario.setComuna(usuarioDTO.getComuna());
        usuario.setRegion(usuarioDTO.getRegion());

        Usuario actualizado = usuarioRepository.save(usuario);
        return convertirADTO(actualizado, false);
    }

    public UsuarioDTO actualizarUsuarioAdmin(long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setDireccion(usuarioDTO.getDireccion());
        usuario.setComuna(usuarioDTO.getComuna());
        usuario.setRegion(usuarioDTO.getRegion());
        usuario.setActivo(usuarioDTO.getActivo());
        usuario.setRol(usuarioDTO.getRol());

        Usuario actualizado = usuarioRepository.save(usuario);
        return convertirADTO(actualizado, false);
    }

    public void eliminarUsuario(long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO convertirADTO(Usuario usuario, boolean cargarHistorial) {
        int historialCompras = 0;
        if (cargarHistorial) {
            historialCompras = boletaRepository.countByUsuarioId(usuario.getId());
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
