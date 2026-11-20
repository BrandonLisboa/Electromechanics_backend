package com.electromechanics.reporte_service.controller;

import com.electromechanics.reporte_service.model.Reporte;
import com.electromechanics.reporte_service.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes", description = "Creacion y consulta de reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Crear reporte (solo trabajador)")
    @PostMapping
    public ResponseEntity<Reporte> crearReporte(@Valid @RequestBody CrearReporteRequest request,
                                                Authentication auth) {
        UUID idTrabajador = UUID.fromString(auth.getName());

        Reporte reporte = new Reporte(idTrabajador, request.titulo(), request.descripcion());
        reporte.setImagenUrl(request.imagenUrl());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reporteService.crearReporte(reporte));
    }

    @Operation(summary = "Ver mis reportes (solo trabajador)")
    @GetMapping("/mios")
    public List<Reporte> misReportes(Authentication auth) {
        return reporteService.buscarPorUsuario(UUID.fromString(auth.getName()));
    }

    @Operation(summary = "Listar todos los reportes (solo supervisor)")
    @GetMapping
    public List<Reporte> listarReportes() {
        return reporteService.listarReportes();
    }

    @Operation(summary = "Buscar reporte por id (solo supervisor)")
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> buscarPorId(@PathVariable Long id) {
        return reporteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Reportes de un trabajador por id (solo supervisor)")
    @GetMapping("/usuario/{usuarioId}")
    public List<Reporte> buscarPorUsuario(@PathVariable UUID usuarioId) {
        return reporteService.buscarPorUsuario(usuarioId);
    }

    @Operation(summary = "Reportes por estado (solo supervisor)")
    @GetMapping("/estado/{estado}")
    public List<Reporte> buscarPorEstado(@PathVariable String estado) {
        return reporteService.buscarPorEstado(estado);
    }

    @Operation(summary = "Cambiar estado de un reporte (solo supervisor)")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reporte> actualizarEstado(@PathVariable Long id,
                                                    @Valid @RequestBody ActualizarEstadoRequest request) {
        return reporteService.actualizarEstado(id, request.estado())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public record CrearReporteRequest(
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