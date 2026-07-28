package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// CRUD padrão para Pagamento; nenhuma consulta customizada é necessária ainda.
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}