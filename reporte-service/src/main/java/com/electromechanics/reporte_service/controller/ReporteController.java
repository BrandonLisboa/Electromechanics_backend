package com.electromechanics.reporte_service.controller;

import com.electromechanics.reporte_service.model.Reporte;
import com.electromechanics.reporte_service.service.ReporteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<List<Reporte>> listarReportes() {
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> buscarPorId(@PathVariable Long id) {
        return reporteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reporte>> buscarPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                reporteService.buscarPorUsuario(usuarioId)
        );
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reporte>> buscarPorEstado(
            @PathVariable String estado) {

        return ResponseEntity.ok(
                reporteService.buscarPorEstado(estado)
        );
    }

    @PostMapping
    public ResponseEntity<Reporte> crearReporte(
            @Valid @RequestBody CrearReporteRequest request) {

        Reporte reporte = new Reporte(
                request.usuarioId(),
                request.titulo(),
                request.descripcion()
        );

        reporte.setImagenUrl(request.imagenUrl());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reporteService.crearReporte(reporte));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reporte> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoRequest request) {

        return ResponseEntity.ok(
                reporteService.actualizarEstado(id, request.estado())
        );
    }

    public record CrearReporteRequest(
            @NotNull Long usuarioId,
            @NotBlank String titulo,
            @NotBlank String descripcion,
            String imagenUrl
    ) {
    }

    public record ActualizarEstadoRequest(
            @NotBlank String estado
    ) {
    }
}
