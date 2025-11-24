package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.AuthResponse;
import com.pasteleriamilsabores.backend.dto.LoginRequest;
import com.pasteleriamilsabores.backend.dto.RegisterRequest;
import com.pasteleriamilsabores.backend.exception.BadRequestException;
import com.pasteleriamilsabores.backend.model.Usuario;
import com.pasteleriamilsabores.backend.repository.UsuarioRepository;
import com.pasteleriamilsabores.backend.security.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BadRequestException("Credenciales inválidas");
        }

        usuario.setUltimaConexion(java.time.LocalDateTime.now());
        usuarioRepository.save(usuario);

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol(), usuario.getId());

        return new AuthResponse(token, usuario.getId(), usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getEmail(), usuario.getRol());
    }

    public AuthResponse register(RegisterRequest request) {
        return crearUsuarioBase(request, "ROLE_CLIENTE");
    }

    public AuthResponse registerAdmin(RegisterRequest request) {
        return crearUsuarioBase(request, "ROLE_ADMIN");
    }

    public AuthResponse registerVendedor(RegisterRequest request) {
        return crearUsuarioBase(request, "ROLE_VENDEDOR");
    }

    private AuthResponse crearUsuarioBase(RegisterRequest request, String rol) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);
        usuario.setRut(request.getRut());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setComuna(request.getComuna());
        usuario.setRegion(request.getRegion() != null && !request.getRegion().isEmpty() ? request.getRegion()
                : "Región Metropolitana");

        Usuario guardado = usuarioRepository.save(usuario);

        String token = jwtUtil.generarToken(guardado.getEmail(), guardado.getRol(), guardado.getId());

        return new AuthResponse(token, guardado.getId(), guardado.getNombre() + " " + guardado.getApellido(),
                guardado.getEmail(), guardado.getRol());
    }
}
