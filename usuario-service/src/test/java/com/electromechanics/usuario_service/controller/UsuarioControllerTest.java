package com.electromechanics.usuario_service.controller;

import com.electromechanics.usuario_service.dto.LoginRequest;
import com.electromechanics.usuario_service.dto.LoginResponse;
import com.electromechanics.usuario_service.dto.RegistroRequest;
import com.electromechanics.usuario_service.dto.UsuarioResponse;
import com.electromechanics.usuario_service.exception.CredencialesInvalidasException;
import com.electromechanics.usuario_service.exception.RutRegistradoException;
import com.electromechanics.usuario_service.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioController = new UsuarioController(usuarioService);
    }

    @Test
    void registrarDevuelve201ConElUsuarioCreado() {
        UUID id = UUID.randomUUID();
        UsuarioResponse esperado = new UsuarioResponse(id, "11111111-1", null, null, "EMPLEADO");
        when(usuarioService.registrar(any(RegistroRequest.class))).thenReturn(esperado);

        ResponseEntity<UsuarioResponse> respuesta =
                usuarioController.registrar(new RegistroRequest("11111111-1", "clave123"));

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(respuesta.getBody()).isEqualTo(esperado);
    }

    @Test
    void registrarSiRutYaExiste() {
        when(usuarioService.registrar(any(RegistroRequest.class)))
                .thenThrow(new RutRegistradoException());

        assertThatThrownBy(() -> usuarioController.registrar(
                new RegistroRequest("11111111-1", "clave123")))
                .isInstanceOf(RutRegistradoException.class);
    }

    @Test
    void loginCorrectoDevuelve200() {
        UUID id = UUID.randomUUID();
        LoginResponse esperado = new LoginResponse(
                "token-de-prueba",
                new UsuarioResponse(id, "11111111-1", null, null, "TRABAJADOR"));
        when(usuarioService.login(any(LoginRequest.class))).thenReturn(esperado);

        ResponseEntity<LoginResponse> respuesta =
                usuarioController.login(new LoginRequest("11111111-1", "clave123"));

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody()).isEqualTo(esperado);
    }

    @Test
    void loginIncorrectoCredencialesInvalidas() {
        when(usuarioService.login(any(LoginRequest.class)))
                .thenThrow(new CredencialesInvalidasException());

        assertThatThrownBy(() -> usuarioController.login(
                new LoginRequest("11111111-1", "mala")))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    void buscarPorIdInexistenteDevuelve404() {
        UUID id = UUID.randomUUID();
        when(usuarioService.buscarPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<?> respuesta = usuarioController.buscarPorId(id);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}