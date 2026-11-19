package com.electromechanics.usuario_service.exception;

public class RutRegistradoException extends RuntimeException {
    public RutRegistradoException() {
        super("El RUT ya está registrado");
    }


}
