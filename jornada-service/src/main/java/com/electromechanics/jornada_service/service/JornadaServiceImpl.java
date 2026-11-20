package com.electromechanics.jornada_service.service;

import com.electromechanics.jornada_service.model.Jornada;
import com.electromechanics.jornada_service.repository.JornadaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JornadaServiceImpl implements JornadaService {

    private final JornadaRepository jornadaRepository;

    public JornadaServiceImpl(JornadaRepository jornadaRepository) {
        this.jornadaRepository = jornadaRepository;
    }

    @Override
    public List<Jornada> listarJornadas() {
        return jornadaRepository.findAll();
    }

    @Override
    public Optional<Jornada> buscarPorId(Long id) {
        return jornadaRepository.findById(id);
    }

    @Override
    public List<Jornada> buscarPorUsuario(UUID usuarioId) {
        return jornadaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Jornada iniciarJornada(UUID usuarioId, Double latitud, Double longitud) {
        boolean jornadaActiva = jornadaRepository.findByUsuarioId(usuarioId)
                .stream()
                .anyMatch(j -> "EN_CURSO".equals(j.getEstado()));

        if (jornadaActiva) {
            throw new IllegalArgumentException("El usuario ya tiene una jornada en curso");
        }

        Jornada jornada = new Jornada(usuarioId, LocalDateTime.now(), "EN_CURSO");
        jornada.setLatitud(latitud);
        jornada.setLongitud(longitud);

        return jornadaRepository.save(jornada);
    }

    @Override
    public Jornada finalizarJornadaActiva(UUID usuarioId) {
        Jornada jornada = jornadaRepository.findFirstByUsuarioIdAndEstado(usuarioId, "EN_CURSO")
                .orElseThrow(() -> new IllegalArgumentException("No tienes una jornada en curso"));

        jornada.setFin(LocalDateTime.now());
        jornada.setEstado("FINALIZADA");

        return jornadaRepository.save(jornada);
    }
}