package com.electromechanics.jornada_service.service;

import com.electromechanics.jornada_service.model.Jornada;
import com.electromechanics.jornada_service.repository.JornadaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public List<Jornada> buscarPorUsuario(Long usuarioId) {
        return jornadaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Jornada iniciarJornada(Long usuarioId, Double latitud, Double longitud) {

        if (usuarioId == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        boolean jornadaActiva = jornadaRepository.findByUsuarioId(usuarioId)
                .stream()
                .anyMatch(jornada -> "EN_CURSO".equals(jornada.getEstado()));

        if (jornadaActiva) {
            throw new IllegalArgumentException(
                    "El usuario ya tiene una jornada en curso"
            );
        }

        Jornada jornada = new Jornada(
                usuarioId,
                LocalDateTime.now(),
                "EN_CURSO"
        );

        jornada.setLatitud(latitud);
        jornada.setLongitud(longitud);

        return jornadaRepository.save(jornada);
    }

    @Override
    public Jornada finalizarJornada(Long id) {

        Jornada jornada = jornadaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Jornada no encontrada"
                ));

        if (!"EN_CURSO".equals(jornada.getEstado())) {
            throw new IllegalArgumentException(
                    "La jornada ya se encuentra finalizada"
            );
        }

        jornada.setFin(LocalDateTime.now());
        jornada.setEstado("FINALIZADA");

        return jornadaRepository.save(jornada);
    }
}
