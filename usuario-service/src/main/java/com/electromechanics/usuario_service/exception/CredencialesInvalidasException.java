package com.electromechanics.usuario_service.exception;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException() {
        super("Rut o contraseña invalidos");
    }

}
