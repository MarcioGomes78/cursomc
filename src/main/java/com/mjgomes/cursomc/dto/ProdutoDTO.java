package com.mjgomes.cursomc.dto;

import com.mjgomes.cursomc.domain.Produto;

import java.io.Serializable;

// Versão exposta na API de Produto, sem as associações de Categoria/ItemPedido.
public class ProdutoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private Double price;

    public ProdutoDTO() {}

    public ProdutoDTO(Produto obj) {
        id = obj.getId();
        name = obj.getName();
        price = obj.getPrice();
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
