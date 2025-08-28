package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.Cliente;
import com.mjgomes.cursomc.repositories.ClienteRepository;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    public Cliente find(Integer id) {

        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found! Id: " + id + ", Type: " + Cliente.class.getName()));
    }
}
