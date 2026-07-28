package com.mjgomes.cursomc.repositories;

import com.mjgomes.cursomc.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Usado no login (UserDetailsServiceImpl) para carregar o Cliente pelo email informado nas credenciais.
    @Transactional(readOnly = true)
    Cliente findByEmail(String email);
}
