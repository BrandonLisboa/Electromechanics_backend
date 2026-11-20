package com.electromechanics.usuario_service.dto;

import com.electromechanics.usuario_service.model.Rol;
import jakarta.validation.constraints.NotNull;

public record CambiarRolRequest(
        @NotNull(message = "El rol es obligatorio") Rol rol
) {}