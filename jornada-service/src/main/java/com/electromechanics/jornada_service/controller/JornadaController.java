package com.electromechanics.jornada_service.controller;

import com.electromechanics.jornada_service.model.Jornada;
import com.electromechanics.jornada_service.service.JornadaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jornadas")
public class JornadaController {

    private final JornadaService jornadaService;

    public JornadaController(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }


    @GetMapping
    public List<Jornada> listarJornadas() {
        return jornadaService.listarJornadas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jornada> buscarPorId(@PathVariable Long id) {
        return jornadaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Jornada> buscarPorUsuario(@PathVariable UUID usuarioId) {
        return jornadaService.buscarPorUsuario(usuarioId);
    }


    @GetMapping("/mias")
    public List<Jornada> misJornadas(Authentication auth) {
        return jornadaService.buscarPorUsuario(UUID.fromString(auth.getName()));
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Jornada> iniciarJornada(@Valid @RequestBody IniciarJornadaRequest request,
                                                  Authentication auth) {
        UUID idTrabajador = UUID.fromString(auth.getName());

        Jornada jornada = jornadaService.iniciarJornada(
                idTrabajador,
                request.latitud(),
                request.longitud());

        return ResponseEntity.status(HttpStatus.CREATED).body(jornada);
    }

    @PutMapping("/finalizar")
    public ResponseEntity<Jornada> finalizarJornada(Authentication auth) {
        UUID idTrabajador = UUID.fromString(auth.getName());
        return ResponseEntity.ok(jornadaService.finalizarJornadaActiva(idTrabajador));
    }

    public record IniciarJornadaRequest(Double latitud, Double longitud) {
    }
}
