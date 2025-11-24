package com.pasteleriamilsabores.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder 50 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^(\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK])$", message = "El RUT debe tener el formato XX.XXX.XXX-X (ej: 12.345.678-9)")
    private String rut;

    private String telefono;

    private String direccion;

    private String comuna;

    private String region;
}
