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

    // Busca paginada por nome (contém, case-sensitive) e categorias; DISTINCT evita duplicar produtos
    // que casem com mais de uma categoria da lista. Poderia virar um derived query method
    // (findDistinctByNameContainingAndCategoriasIn), mas exigiria renomear a chamada em ProdutoService.
    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.name LIKE %:nome% AND cat IN :categorias")
    Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);

}
