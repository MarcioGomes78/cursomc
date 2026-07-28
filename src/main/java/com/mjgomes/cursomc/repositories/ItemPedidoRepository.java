package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.ItemPedido;
import com.mjgomes.cursomc.domain.ItemPediodPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// CRUD padrão para ItemPedido; usa ItemPediodPK (pedido + produto) como chave, não um Integer.
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPediodPK> {
}