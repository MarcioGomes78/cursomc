package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.ItemPedido;
import com.mjgomes.cursomc.domain.PagamentoComBoleto;
import com.mjgomes.cursomc.domain.Pedido;
import com.mjgomes.cursomc.enums.EstadoPagamento;
import com.mjgomes.cursomc.repositories.ItemPedidoRepository;
import com.mjgomes.cursomc.repositories.PagamentoRepository;
import com.mjgomes.cursomc.repositories.PedidoRepository;
import com.mjgomes.cursomc.repositories.ProdutoRepository;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

// Orquestra a criação de um Pedido: resolve associações (cliente/produtos), gera o pagamento pendente,
// congela preço/desconto de cada item e dispara o e-mail de confirmação.
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;
    @Autowired
    private BoletoService boletoService;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private EmailService emailService;

    public Pedido find(Integer id) {

        Optional<Pedido> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found! Id: " + id + ", Type: " + Pedido.class.getName()));
    }

    // @Transactional: todo o pedido (pagamento + itens) é persistido atomicamente antes do e-mail ser disparado.
    @Transactional
    public Pedido insert(Pedido obj) {
        obj.setId(null);
        obj.setInstante(new Date());
        // Recarrega o cliente pelo id para garantir que é uma entidade gerenciada/existente, não o que veio no JSON.
        obj.setCliente(clienteService.find(obj.getCliente().getId()));
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if (obj.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());

        }
        obj = repo.save(obj);
        pagamentoRepository.save(obj.getPagamento());
        // Preço/desconto de cada item são fixados no momento da compra (ver ItemPedido.getSubTotal),
        // então recarregamos o Produto aqui só para copiar o preço atual, não para manter a referência viva.
        for (ItemPedido ip : obj.getItens()) {
            ip.setDesconto(0.0);
            ip.setProduto(produtoService.find(ip.getProduto().getId()));
            ip.setPreco(ip.getProduto().getPrice());
            ip.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());
        emailService.sendOrderConfirmationHtmlEmail(obj);
        return obj;
    }
}