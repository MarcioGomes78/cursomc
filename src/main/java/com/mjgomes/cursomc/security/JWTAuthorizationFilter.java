package com.mjgomes.cursomc.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filtro responsável por AUTORIZAÇÃO: a cada requisição, valida o token JWT
// enviado pelo cliente e, se for válido, autentica o usuário no contexto de segurança.
// Diferente do JWTAuthenticationFilter (que faz o LOGIN e gera o token).
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    // Utilitário usado para validar o token e extrair o usuário (subject) dele
    private final JWTUtil jwtUtil;
    // Usado para carregar os dados/permissões do usuário a partir do username extraído do token
    private final UserDetailsService userDetailsService;

    // Construtor: recebe as dependências necessárias para validar o token
    // e repassa o AuthenticationManager para a classe pai (BasicAuthenticationFilter)
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if(jwtUtil.tokenValido(token)){
            String username = jwtUtil.getUsername(token);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }   
        return null;
    }
    
}