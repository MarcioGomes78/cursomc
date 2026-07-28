package com.mjgomes.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.mjgomes.cursomc.domain.Cliente;
import com.mjgomes.cursomc.dto.ClienteDTO;
import com.mjgomes.cursomc.repositories.ClienteRepository;
import com.mjgomes.cursomc.resources.exception.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

    // Obtem o parametro da URI.
    private final HttpServletRequest request;

    @Autowired
    private ClienteRepository repo;

    ClienteUpdateValidator(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void initialize(ClienteUpdate ann) {
    }

    @Override
    public boolean isValid(ClienteDTO objDto, jakarta.validation.ConstraintValidatorContext context) {

        // O DTO não traz o id do cliente sendo atualizado, então ele é lido do path variable {id} da própria
        // requisição (injetado pelo Spring como atributo do request) para permitir comparar com aux.getId().
        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        // Só é erro se o email já pertencer a OUTRO cliente; o próprio cliente pode manter seu email atual.
        Cliente aux = repo.findByEmail(objDto.getEmail());
        if (aux != null && !aux.getId().equals(uriId)) {
            list.add(new FieldMessage("email", "Email já existente"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
