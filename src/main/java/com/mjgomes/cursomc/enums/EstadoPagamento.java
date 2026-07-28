package com.mjgomes.cursomc.enums;

// Situação de um Pagamento; persistido como o "cod" (Integer), nunca o nome do enum.
public enum EstadoPagamento {

    PENDENTE(1, "Pendente"),
    QUITADO(2, "Quitado"),
    CANCELADO(3, "Cancelado");

    private Integer cod;
    private String descricao;

    private EstadoPagamento(Integer cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public Integer getCod() {
        return cod;
    }
    public String getDescricao() {
        return descricao;
    }

    // Converte o código persistido de volta para o enum; lança exceção se vier um código desconhecido.
    public static EstadoPagamento toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (EstadoPagamento x : EstadoPagamento.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }

}
