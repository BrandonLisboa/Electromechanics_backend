package com.electromechanics.usuario_service.dto;

import java.util.UUID;

public record UsuarioResponse(UUID id, String rut, String nombre, String correo, String rol) {

}
