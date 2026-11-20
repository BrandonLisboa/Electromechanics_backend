package com.electromechanics.reporte_service.service;

import com.electromechanics.reporte_service.model.Reporte;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReporteService {

    List<Reporte> listarReportes();

    Optional<Reporte> buscarPorId(Long id);

    List<Reporte> buscarPorUsuario(UUID usuarioId);

    List<Reporte> buscarPorEstado(String estado);

    Reporte crearReporte(Reporte reporte);

    Optional<Reporte> actualizarEstado(Long id, String estado);
}