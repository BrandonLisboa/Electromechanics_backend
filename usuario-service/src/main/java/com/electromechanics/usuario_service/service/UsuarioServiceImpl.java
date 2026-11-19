package com.electromechanics.usuario_service.service;

import com.electromechanics.usuario_service.dto.LoginRequest;
import com.electromechanics.usuario_service.dto.LoginResponse;
import com.electromechanics.usuario_service.dto.RegistroRequest;
import com.electromechanics.usuario_service.dto.UsuarioResponse;
import com.electromechanics.usuario_service.exception.CredencialesInvalidasException;
import com.electromechanics.usuario_service.exception.RutRegistradoException;
import com.electromechanics.usuario_service.model.Usuario;
import com.electromechanics.usuario_service.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                            PasswordEncoder passwordEncoder,
                            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorRut(String rut) {
        return usuarioRepository.findByRut(rut.trim());
    }

    @Override
    public UsuarioResponse registrar(RegistroRequest request) {
        String rut = request.rut().trim();

        if (usuarioRepository.existsByRut(rut)) {
            throw new RutRegistradoException();
        }

        Usuario usuario = new Usuario(rut, passwordEncoder.encode(request.password().trim()));
        return toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String rut = request.rut().trim();
        String password = request.password().trim();

        Usuario usuario = usuarioRepository.findByRut(rut)
                .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }

        return new LoginResponse(
                jwtService.generarToken(usuario),
                toResponse(usuario));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getRut(), u.getNombre(), u.getCorreo(), u.getRol().name());
    }
}