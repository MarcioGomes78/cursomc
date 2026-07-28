package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.PagamentoComBoleto;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

// Preenche os dados do boleto no momento da criação do Pedido (regra de negócio isolada do PedidoService).
@Service
public class BoletoService {

    public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(instanteDoPedido);
        cal.add(Calendar.DAY_OF_MONTH, 7); // Vencimento em 7 dias
        pagto.setDataVencimento(cal.getTime());
    }
}
