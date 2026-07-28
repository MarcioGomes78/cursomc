package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.Categoria;
import com.mjgomes.cursomc.dto.CategoriaDTO;
import com.mjgomes.cursomc.repositories.CategoriaRepository;
import com.mjgomes.cursomc.services.exceptions.DataIntegrityException;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Regras de negócio de Categoria: CRUD sobre CategoriaRepository e conversão DTO <-> entidade.
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
        Categoria newObj = find(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    // find(id) garante 404 (ObjectNotFoundException) se o id não existir, antes de tentar excluir.
    // A FK de produto_categoria vira DataIntegrityViolationException, traduzida para uma mensagem amigável.
    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
        }
        repo.deleteById(id);
    }

    public List<Categoria> findAll() {
        return repo.findAll();
    }

    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        // PageRequest é uma classe do Spring Data que implementa a interface Pageable,
        // e que prepara e ar mazena as informações de paginação para serem enviadas ao repositório.
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    // Constrói a entidade a partir do DTO recebido na API (sem tocar nas associações de Produto).
    public Categoria fromDTO(@Valid CategoriaDTO objDto) {
        return new Categoria(objDto.getId(), objDto.getName());
    }

    private  void updateData(Categoria newObj, Categoria objDto) {
        newObj.setName(objDto.getName());
    }
}
