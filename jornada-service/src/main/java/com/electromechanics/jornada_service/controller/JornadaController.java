package com.electromechanics.jornada_service.controller;

import com.electromechanics.jornada_service.model.Jornada;
import com.electromechanics.jornada_service.service.JornadaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jornadas")
public class JornadaController {

    private final JornadaService jornadaService;

    public JornadaController(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public ResponseEntity<List<Jornada>> listarJornadas() {
        return ResponseEntity.ok(jornadaService.listarJornadas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jornada> buscarPorId(@PathVariable Long id) {
        return jornadaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Jornada>> buscarPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                jornadaService.buscarPorUsuario(usuarioId)
        );
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Jornada> iniciarJornada(
            @Valid @RequestBody IniciarJornadaRequest request) {

        Jornada jornada = jornadaService.iniciarJornada(
                request.usuarioId(),
                request.latitud(),
                request.longitud()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(jornada);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Jornada> finalizarJornada(@PathVariable Long id) {
        return ResponseEntity.ok(jornadaService.finalizarJornada(id));
    }

    public record IniciarJornadaRequest(
            @NotNull Long usuarioId,
            Double latitud,
            Double longitud
    ) {
    }
}
