package com.electromechanics.jornada_service.service;

import com.electromechanics.jornada_service.model.Jornada;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JornadaService {

    List<Jornada> listarJornadas();

    Optional<Jornada> buscarPorId(Long id);

    List<Jornada> buscarPorUsuario(UUID usuarioId);

    Jornada iniciarJornada(UUID usuarioId, Double latitud, Double longitud);

    Jornada finalizarJornadaActiva(UUID usuarioId);
}