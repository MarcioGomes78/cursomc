package com.mjgomes.cursomc.services.exceptions;

// Lançada quando uma operação violaria a integridade dos dados (ex: excluir um registro ainda referenciado);
// traduzida para HTTP 400 em ResourceExceptionHandler.
public class DataIntegrityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataIntegrityException(String msg) {
        super(msg);
    }

    public DataIntegrityException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
