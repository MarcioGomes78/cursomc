package com.mjgomes.cursomc.services.exceptions;

// Lançada quando um find(id) não encontra o registro; traduzida para HTTP 404 em ResourceExceptionHandler.
public class ObjectNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String msg) {
        super(msg);
    }

    public ObjectNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
