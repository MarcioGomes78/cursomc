package com.mjgomes.cursomc.services.exceptions;

// Lançada pelos services quando o usuário autenticado não tem permissão para a operação;
// tratada pelo ResourceExceptionHandler, que converte para HTTP 403.
public class AuthorizationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AuthorizationException(String msg) {
        super(msg);
    }

    public AuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
