package com.electromechanics.usuario_service.service;

import com.electromechanics.usuario_service.model.Usuario;
import com.electromechanics.usuario_service.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorRut(String rut) {
        return usuarioRepository.findByRut(rut);
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {

        if (usuarioRepository.existsByRut(usuario.getRut())) {
            throw new IllegalArgumentException("El RUT ya está registrado");
        }

        return usuarioRepository.save(usuario);
    }
}
