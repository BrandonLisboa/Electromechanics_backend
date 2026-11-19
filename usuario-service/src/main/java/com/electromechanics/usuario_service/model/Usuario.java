package com.electromechanics.usuario_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter 
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Getter 
    @Setter 

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String rut;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String nombre;

    @Email
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol = Rol.TRABAJADOR;

    public Rol getRol() { return rol; }
    
    public void setRol(Rol rol) { this.rol = rol; }

    public Usuario() {
    }

    public Usuario(String rut, String passwordHash) {
        this.rut = rut;
        this.passwordHash = passwordHash;
    }

    
}