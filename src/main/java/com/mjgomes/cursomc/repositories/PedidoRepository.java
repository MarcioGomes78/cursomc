package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// CRUD padrão para Pedido; nenhuma consulta customizada é necessária ainda.
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {}