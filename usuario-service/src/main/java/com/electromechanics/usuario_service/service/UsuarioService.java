package com.electromechanics.usuario_service.service;

import com.electromechanics.usuario_service.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listarUsuarios();

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorRut(String rut);

    Usuario guardarUsuario(Usuario usuario);
}
