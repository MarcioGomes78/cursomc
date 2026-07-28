package com.mjgomes.cursomc.services.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Bean Validation customizada aplicada a ClienteNewDTO: delega a checagem de CPF/CNPJ e email
// duplicado para ClienteInsertValidator.
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClienteInsertValidator.class)
public @interface ClienteInsert {
    // Mensagem padrão; na prática cada erro real é adicionado via context.buildConstraintViolationWithTemplate
    // em ClienteInsertValidator, então esta mensagem genérica raramente é exibida.
    String message() default "Erro de validação";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
