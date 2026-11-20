package com.electromechanics.jornada_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter 
@Setter 
@Table(name = "jornadas")
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime inicio;

    private LocalDateTime fin;

    @Column(nullable = false)
    private String estado;

    private Double latitud;

    private Double longitud;

    public Jornada() {
    }

    public Jornada(UUID usuarioId, LocalDateTime inicio, String estado) {
        this.usuarioId = usuarioId;
        this.inicio = inicio;
        this.estado = estado;
    }

}