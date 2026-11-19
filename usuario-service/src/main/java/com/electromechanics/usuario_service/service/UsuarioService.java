package com.electromechanics.usuario_service.service;

import com.electromechanics.usuario_service.dto.LoginRequest;
import com.electromechanics.usuario_service.dto.LoginResponse;
import com.electromechanics.usuario_service.dto.RegistroRequest;
import com.electromechanics.usuario_service.dto.UsuarioResponse;
import com.electromechanics.usuario_service.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService {

    List<Usuario> listarUsuarios();

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarPorRut(String rut);

    UsuarioResponse registrar(RegistroRequest request);

    LoginResponse login(LoginRequest request);
}