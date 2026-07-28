package com.mjgomes.cursomc.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mjgomes.cursomc.enums.EstadoPagamento;
import jakarta.persistence.Entity;

// Pagamento via cartão de crédito: guarda apenas o número de parcelas escolhido.
@Entity
@JsonTypeName("pagamentoComCartao")
public class PagamentoComCartao  extends Pagamento {
    private static final long serialVersionUID = 1L;

    private Integer numeroDeParcelas;

    // Empty constructor.
    public PagamentoComCartao() {
    }

    public PagamentoComCartao(Integer id, EstadoPagamento estado, Pedido pedido, Integer numeroDeParcelas) {
        super(id, estado, pedido);
        this.numeroDeParcelas = numeroDeParcelas;
    }

    public Integer getNumeroDeParcelas() {
        return numeroDeParcelas;
    }

    public void setNumeroDeParcelas(Integer numeroDeParcelas) {
        this.numeroDeParcelas = numeroDeParcelas;
    }
}
