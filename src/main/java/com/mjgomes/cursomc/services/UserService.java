package com.mjgomes.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mjgomes.cursomc.security.UserSS;

// Acesso ao usuário logado a partir do contexto de segurança, usado pelos services para checagens de autorização.
@Service
public class UserService {

    // Recupera o UserSS do token JWT já validado pelo JWTAuthorizationFilter; retorna null se não houver
    // usuário autenticado (ex: endpoint público) ou se o principal não for do tipo esperado.
    public static UserSS authenticated() {
        try {
            return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
