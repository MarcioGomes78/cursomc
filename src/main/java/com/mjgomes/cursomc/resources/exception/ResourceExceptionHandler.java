package com.mjgomes.cursomc.resources.exception;

import com.mjgomes.cursomc.services.exceptions.DataIntegrityException;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Traduz as exceções internas dos services em respostas HTTP com corpo padronizado (StandardError/ValidationError),
// centralizando esse mapeamento em vez de repeti-lo em cada Resource.
@ControllerAdvice
public class ResourceExceptionHandler {

    // Entidade não encontrada (ex: find por id inexistente) -> 404.
    @ExceptionHandler
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {

        StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    // Violação de integridade (ex: excluir um registro referenciado por outro) -> 400.
    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> objectNotFound(DataIntegrityException e, HttpServletRequest request) {

        StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // Falha de Bean Validation (@Valid) num @RequestBody -> 400 com a lista de campos inválidos.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

        ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de Validação", System.currentTimeMillis());
        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            err.addError(x.getField(), x.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
