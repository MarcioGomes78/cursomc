package com.mjgomes.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mjgomes.cursomc.domain.Cidade;
import com.mjgomes.cursomc.repositories.CidadeRepository;

// Componente de serviço para operações de negócio relacionadas a Cidades
@Service
public class CidadeService {

    // Injeção de dependência do repositório
    @Autowired
    private CidadeRepository repo;

    // Método para buscar cidades por estado
    public List<Cidade> findByEstado(Integer estadoId) {

        // O repositório cuida da transação e otimização automática para SELECTs
        return repo.findCidades(estadoId);
    }
}
