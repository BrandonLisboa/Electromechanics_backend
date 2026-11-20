package com.electromechanics.usuario_service.exception;

public class AccionNoPermitidaException extends RuntimeException {
    public AccionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}