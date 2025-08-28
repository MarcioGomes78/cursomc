package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.Pedido;
import com.mjgomes.cursomc.repositories.PedidoRepository;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    public Pedido find(Integer id) {

        Optional<Pedido> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found! Id: " + id + ", Type: " + Pedido.class.getName()));
    }
}
