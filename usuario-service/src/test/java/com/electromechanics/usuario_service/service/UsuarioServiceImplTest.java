package com.electromechanics.usuario_service.service;

import com.electromechanics.usuario_service.dto.LoginRequest;
import com.electromechanics.usuario_service.dto.RegistroRequest;
import com.electromechanics.usuario_service.dto.UsuarioResponse;
import com.electromechanics.usuario_service.exception.CredencialesInvalidasException;
import com.electromechanics.usuario_service.exception.RutRegistradoException;
import com.electromechanics.usuario_service.model.Usuario;
import com.electromechanics.usuario_service.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private PasswordEncoder passwordEncoder;
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        usuarioService = new UsuarioServiceImpl(usuarioRepository, passwordEncoder);
    }

    @Test
    void registrarGuardaUsuarioConContrasenaCifrada() {
        when(usuarioRepository.existsByRut("11111111-1")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        UsuarioResponse respuesta = usuarioService.registrar(
                new RegistroRequest("11111111-1", "clave123"));

        assertThat(respuesta.id()).isNotNull();
        assertThat(respuesta.rut()).isEqualTo("11111111-1");
        assertThat(respuesta.rol()).isEqualTo("TRABAJADOR");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        String hashGuardado = captor.getValue().getPasswordHash();

        assertThat(hashGuardado).isNotEqualTo("clave123");
        assertThat(passwordEncoder.matches("clave123", hashGuardado)).isTrue();
    }

    @Test
    void registrarConRutRepetidoLanzaExcepcionYNoGuarda() {
        when(usuarioRepository.existsByRut("11111111-1")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.registrar(
                new RegistroRequest("11111111-1", "clave123")))
                .isInstanceOf(RutRegistradoException.class);

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void loginConCredencialesCorrectasDevuelveUsuario() {
        Usuario existente = new Usuario("11111111-1", passwordEncoder.encode("clave123"));
        existente.setId(UUID.randomUUID());
        when(usuarioRepository.findByRut("11111111-1")).thenReturn(Optional.of(existente));

        UsuarioResponse respuesta = usuarioService.login(
                new LoginRequest("11111111-1", "clave123"));

        assertThat(respuesta.id()).isEqualTo(existente.getId());
        assertThat(respuesta.rut()).isEqualTo("11111111-1");
    }

    @Test
    void loginConContrasenaIncorrectaLanzaExcepcion() {
        Usuario existente = new Usuario("11111111-1", passwordEncoder.encode("clave123"));
        when(usuarioRepository.findByRut("11111111-1")).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> usuarioService.login(
                new LoginRequest("11111111-1", "otra-clave")))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    void loginConRutInexistenteLanzaExcepcion() {
        when(usuarioRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.login(
                new LoginRequest("99999999-9", "clave123")))
                .isInstanceOf(CredencialesInvalidasException.class);
    }
}