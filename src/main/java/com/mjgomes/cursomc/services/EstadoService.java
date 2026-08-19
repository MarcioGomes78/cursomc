package com.mjgomes.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mjgomes.cursomc.domain.Estado;
import com.mjgomes.cursomc.repositories.EstadoRepository;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository repo;

    public List<Estado> findAll() {
        return repo.findAllByOrderByName();
    }

}
