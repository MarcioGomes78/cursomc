package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// CRUD padrão para Cidade; nenhuma consulta customizada é necessária ainda.
@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
}
