package com.mjgomes.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import com.mjgomes.cursomc.domain.Cliente;
import com.mjgomes.cursomc.dto.ClienteNewDTO;
import com.mjgomes.cursomc.enums.TipoCliente;
import com.mjgomes.cursomc.repositories.ClienteRepository;
import com.mjgomes.cursomc.resources.exception.FieldMessage;
import com.mjgomes.cursomc.services.validation.utils.BR;

import jakarta.validation.ConstraintValidator;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

    private final ClienteRepository repo;

    ClienteInsertValidator(ClienteRepository repo) {
        this.repo = repo;
    }

    @Override
    public void initialize(ClienteInsert ann) {
    }

    @Override
    public boolean isValid(ClienteNewDTO objDto, jakarta.validation.ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();
        // O algoritmo de validação (dígito verificador) depende do tipo de cliente escolhido.
        if (objDto.getTipo().equals(TipoCliente.PESSOAFISICAS.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }
        if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }

        Cliente aux = repo.findByEmail(objDto.getEmail());
        if (aux != null) {
            list.add(new FieldMessage("email", "Email já existente"));
        }

        // Cada FieldMessage vira uma violação de constraint associada ao campo correspondente,
        // para que o erro apareça no JSON de resposta (ver ResourceExceptionHandler) já ligado ao campo certo.
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
