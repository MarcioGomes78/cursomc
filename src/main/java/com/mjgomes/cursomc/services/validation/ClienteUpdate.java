package com.mjgomes.cursomc.services.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Bean Validation customizada aplicada a ClienteDTO: delega a checagem de email duplicado
// (ignorando o próprio cliente sendo atualizado) para ClienteUpdateValidator.
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClienteUpdateValidator.class)

public @interface ClienteUpdate {
    String message() default "Erro de validação";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
