package com.mjgomes.cursomc.dto;

import org.hibernate.validator.constraints.Length;

import com.mjgomes.cursomc.domain.Cidade;

import jakarta.validation.constraints.NotEmpty;

public class CidadeDTO {

    private Integer id;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String name;

    public CidadeDTO() {}

	public CidadeDTO(Cidade obj) {
		id = obj.getId();
		name = obj.getName();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
