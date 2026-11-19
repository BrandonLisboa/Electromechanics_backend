package com.electromechanics.usuario_service.repository;

import com.electromechanics.usuario_service.model.Rol;
import com.electromechanics.usuario_service.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void guardaYBuscaUsuarioPorRut() {
        usuarioRepository.save(new Usuario("11111111-1", "hash-de-prueba"));

        assertThat(usuarioRepository.findByRut("11111111-1")).isPresent();
        assertThat(usuarioRepository.findByRut("99999999-9")).isEmpty();
    }

    @Test
    void existsByRutDetectaRutGuardado() {
        usuarioRepository.save(new Usuario("11111111-1", "hash-de-prueba"));

        assertThat(usuarioRepository.existsByRut("11111111-1")).isTrue();
        assertThat(usuarioRepository.existsByRut("99999999-9")).isFalse();
    }

    @Test
    void rolPorDefectoEsTrabajador() {
        Usuario guardado = usuarioRepository.save(new Usuario("11111111-1", "hash-de-prueba"));

        assertThat(guardado.getRol()).isEqualTo(Rol.TRABAJADOR);
    }
}