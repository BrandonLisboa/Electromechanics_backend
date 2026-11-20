package com.electromechanics.reporte_service.repository;

import com.electromechanics.reporte_service.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByUsuarioId(UUID usuarioId);

    List<Reporte> findByEstado(String estado);
}