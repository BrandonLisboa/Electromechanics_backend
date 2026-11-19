package com.electromechanics.usuario_service.service;

import com.electromechanics.usuario_service.model.Rol;
import com.electromechanics.usuario_service.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    // HS256 exige al menos 32 caracteres (256 bits)
    private static final String CLAVE = "clave-de-prueba-de-al-menos-32-caracteres-123";
    private static final long UNA_HORA_MS = 3_600_000L;

    private JwtService jwtService;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(CLAVE, UNA_HORA_MS);

        usuario = new Usuario("11111111-1", "hash-de-prueba");
        usuario.setId(UUID.randomUUID());
        usuario.setRol(Rol.TRABAJADOR);
    }

    @Test
    void tokenContieneIdDelUsuario() {
        String token = jwtService.generarToken(usuario);

        Claims claims = jwtService.validarToken(token);

        assertThat(claims.getSubject()).isEqualTo(usuario.getId().toString());
    }

    @Test
    void tokenContieneElRolDelUsuario() {
        String token = jwtService.generarToken(usuario);

        Claims claims = jwtService.validarToken(token);

        assertThat(claims.get("rol", String.class)).isEqualTo("TRABAJADOR");
    }

    @Test
    void tokenDeSupervisorTieneRolSupervisor() {
        usuario.setRol(Rol.SUPERVISOR);
        String token = jwtService.generarToken(usuario);

        Claims claims = jwtService.validarToken(token);

        assertThat(claims.get("rol", String.class)).isEqualTo("SUPERVISOR");
    }

    @Test
    void tokenAlteradoEsRechazado() {
        String token = jwtService.generarToken(usuario);

        // Alteramos un carácter de la mitad de la firma, donde cada bit cuenta
        int posicion = token.lastIndexOf('.') + 10;
        char original = token.charAt(posicion);
        char reemplazo = original == 'A' ? 'B' : 'A';
        String alterado = token.substring(0, posicion) + reemplazo + token.substring(posicion + 1);

        assertThatThrownBy(() -> jwtService.validarToken(alterado))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void tokenFirmadoConOtraClaveEsRechazado() {
        String token = jwtService.generarToken(usuario);
        JwtService otraClave = new JwtService("otra-clave-distinta-de-al-menos-32-caracteres", UNA_HORA_MS);

        assertThatThrownBy(() -> otraClave.validarToken(token))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void tokenVencidoEsRechazado() {
        // Expiración en el pasado: el token nace ya vencido
        JwtService vencido = new JwtService(CLAVE, -1_000L);
        String token = vencido.generarToken(usuario);

        assertThatThrownBy(() -> vencido.validarToken(token))
                .isInstanceOf(JwtException.class);
    }
}