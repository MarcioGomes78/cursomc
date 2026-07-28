package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.Categoria;
import com.mjgomes.cursomc.domain.Produto;
import com.mjgomes.cursomc.repositories.ProdutoRepository;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Regras de negócio de Produto: busca por id e busca paginada por nome/categorias.
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repo;
    @Autowired
    private CategoriaService categoriaService;

    public Produto find(Integer id) {
        Optional<Produto> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found! Id: " + id + ", Type: " + Produto.class.getName()));
    }

    // Nota: "ids" (os ids de categoria vindos da URL) não é usado hoje — a busca sempre considera
    // TODAS as categorias cadastradas, em vez de filtrar pelas categorias informadas.
    public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
        // PageRequest é uma classe do Spring Data que implementa a interface Pageable,
        // e que prepara e ar mazena as informações de paginação para serem enviadas ao repositório.
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        List<Categoria> categorias = categoriaService.findAll();
        return repo.search(nome, categorias, pageRequest);
    }
}
