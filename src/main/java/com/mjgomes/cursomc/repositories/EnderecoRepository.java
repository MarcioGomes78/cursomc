package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// CRUD padrão para Endereco; nenhuma consulta customizada é necessária ainda.
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
}
