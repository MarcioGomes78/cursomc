package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// CRUD padrão para Categoria; nenhuma consulta customizada é necessária ainda.
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
