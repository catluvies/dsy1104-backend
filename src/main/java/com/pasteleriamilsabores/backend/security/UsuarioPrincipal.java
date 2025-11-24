package com.pasteleriamilsabores.backend.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioPrincipal {
    private Long id;
    private String email;
    private String rol;
}
