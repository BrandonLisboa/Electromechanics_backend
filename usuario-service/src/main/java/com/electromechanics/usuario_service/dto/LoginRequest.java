package com.electromechanics.usuario_service.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "El rut es obligatorio") String rut,
    @NotBlank(message = "La contraseña es obligatoria") String password
) {}