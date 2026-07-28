package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Pedido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mjgomes.cursomc.domain.Cliente;

import org.springframework.transaction.annotation.Transactional;

// CRUD padrão para Pedido; nenhuma consulta customizada é necessária ainda.
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    @Transactional(readOnly = true)
    Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
}