package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.PagamentoComBoleto;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class BoletoService {

    public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
        // Lógica para preencher os dados do boleto, como data de vencimento.
        Calendar cal = Calendar.getInstance();
        cal.setTime(instanteDoPedido);
        cal.add(Calendar.DAY_OF_MONTH, 7); // Vencimento em 7 dias
        pagto.setDataVencimento(cal.getTime());
    }
}
