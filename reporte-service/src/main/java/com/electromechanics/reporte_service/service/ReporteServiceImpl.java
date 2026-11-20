package com.electromechanics.reporte_service.service;

import com.electromechanics.reporte_service.model.Reporte;
import com.electromechanics.reporte_service.repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final Set<String> ESTADOS_VALIDOS =
            Set.of("PENDIENTE", "EN_PROCESO", "RESUELTO");

    private final ReporteRepository reporteRepository;

    public ReporteServiceImpl(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @Override
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }

    @Override
    public Optional<Reporte> buscarPorId(Long id) {
        return reporteRepository.findById(id);
    }

    @Override
    public List<Reporte> buscarPorUsuario(UUID usuarioId) {
        return reporteRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Reporte> buscarPorEstado(String estado) {
        validarEstado(estado);
        return reporteRepository.findByEstado(estado);
    }

    @Override
    public Reporte crearReporte(Reporte reporte) {
        if (reporte.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
        if (reporte.getTitulo() == null || reporte.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio");
        }
        if (reporte.getDescripcion() == null || reporte.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es obligatoria");
        }

        reporte.setEstado("PENDIENTE");
        reporte.setFechaCreacion(LocalDateTime.now());

        return reporteRepository.save(reporte);
    }

    @Override
    public Optional<Reporte> actualizarEstado(Long id, String estado) {
        validarEstado(estado);

        return reporteRepository.findById(id).map(reporte -> {
            reporte.setEstado(estado);
            return reporteRepository.save(reporte);
        });
    }

    private void validarEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException(
                    "Estado invalido. Valores permitidos: PENDIENTE, EN_PROCESO, RESUELTO");
        }
    }
}