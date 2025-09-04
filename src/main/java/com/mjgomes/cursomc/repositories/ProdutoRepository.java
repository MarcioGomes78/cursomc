package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Categoria;
import com.mjgomes.cursomc.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.name LIKE %:nome% AND cat IN :categorias")
        // ou Page<Produto> findDistinctByNameContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
    // mudar o nome do metodo "search" no ProdutoService para "findDistinctByNameContainingAndCategoriasIn"
    Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);

}
