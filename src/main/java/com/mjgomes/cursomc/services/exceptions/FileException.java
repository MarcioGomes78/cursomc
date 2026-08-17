package com.mjgomes.cursomc.services.exceptions;

// Exceção para tratamento de erros relacionados a arquivos.
// Lançada pelos services quando o usuário autenticado não tem permissão para a operação;
// tratada pelo ResourceExceptionHandler, que converte para HTTP 403.
public class FileException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileException(String msg) {
        super(msg);
    }

    public FileException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
