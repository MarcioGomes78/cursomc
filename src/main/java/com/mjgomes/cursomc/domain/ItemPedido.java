package com.mjgomes.cursomc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class ItemPedido  implements Serializable {
    private static final long serialVersionUID = 1L;

    // EmbeddedId indicates that this attribute is a compound primary key.
    @JsonIgnore
    @EmbeddedId
    // Composite primary key class for ItemPedido entity
    private  ItemPediodPK id = new ItemPediodPK();

    private Double desconto;
    private Double preco;
    private Integer quantidade;

    public ItemPedido() {}

    public ItemPedido(Pedido pedido, Produto produto, Double desconto, Double preco, Integer quantidade) {
        id.setPedido(pedido);
        id.setProduto(produto);
        this.desconto = desconto;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public double getSubTotal() {
        return (preco - desconto) * quantidade;
    }

    @JsonIgnore
    // For convenience, we provide direct access to Pedido through ItemPedido.
    public Pedido getPedido() {
        return id.getPedido();
    }

    // For convenience, we provide direct access to Produto through ItemPedido.
    public Produto getProduto() {
        return id.getProduto();
    }

    public ItemPediodPK getId() {
        return id;
    }

    public void setId(ItemPediodPK id) {
        this.id = id;
    }

    public Double getDeasconto() {
        return desconto;
    }

    public void setDeasconto(Double deasconto) {
        this.desconto = deasconto;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
