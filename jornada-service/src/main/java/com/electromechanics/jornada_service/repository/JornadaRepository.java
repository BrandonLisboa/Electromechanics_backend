package com.electromechanics.jornada_service.repository;

import com.electromechanics.jornada_service.model.Jornada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {

    List<Jornada> findByUsuarioId(Long usuarioId);

    List<Jornada> findByEstado(String estado);

}
