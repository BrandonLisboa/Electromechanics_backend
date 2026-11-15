package com.electromechanics.jornada_service.service;

import com.electromechanics.jornada_service.model.Jornada;
import java.util.List;
import java.util.Optional;

public interface JornadaService {

    List<Jornada> listarJornadas();

    Optional<Jornada> buscarPorId(Long id);

    List<Jornada> buscarPorUsuario(Long usuarioId);

    Jornada iniciarJornada(Long usuarioId, Double latitud, Double longitud);

    Jornada finalizarJornada(Long id);

}
