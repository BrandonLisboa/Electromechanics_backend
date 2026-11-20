package com.electromechanics.jornada_service.repository;

import com.electromechanics.jornada_service.model.Jornada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {

    List<Jornada> findByUsuarioId(UUID usuarioId);

    List<Jornada> findByEstado(String estado);

    Optional<Jornada> findFirstByUsuarioIdAndEstado(UUID usuarioId, String estado);

}
