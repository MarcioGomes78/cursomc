package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.Categoria;
import com.mjgomes.cursomc.repositories.CategoriaRepository;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repo;

    public Categoria find(Integer id) {

        Optional<Categoria> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found! Id: " + id + ", Type: " + Categoria.class.getName()));
    }

    // Quando o id é null ele insere um novo objeto.
    public Categoria insert(Categoria obj) {
        obj.setId(null);
        return repo.save(obj);
    }

    // quando o id não é null ele atualiza o objeto.
    public Categoria update(Categoria obj) {
        find(obj.getId());
        return repo.save(obj);
    }
}
