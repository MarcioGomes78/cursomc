package com.mjgomes.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

// StandardError especializado para erros de validação de Bean Validation (@Valid): acrescenta a lista
// de campos que falharam, um FieldMessage por campo inválido.
public class ValidationError extends StandardError {
    private static final long serialVersionUID = 1L;

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Integer status, String msg, Long timeStamp) {
        super(status, msg, timeStamp);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String field, String message) {
        errors.add(new FieldMessage(field, message));
    }
}
