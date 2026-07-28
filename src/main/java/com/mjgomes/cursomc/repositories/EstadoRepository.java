package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// CRUD padrão para Estado; nenhuma consulta customizada é necessária ainda.
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}
