package com.electromechanics.usuario_service.repository;

import com.electromechanics.usuario_service.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByRut(String rut);

    boolean existsByRut(String rut);
}
