package com.mjgomes.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjgomes.cursomc.dto.CredenciaisDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filtro responsável pelo LOGIN: intercepta a requisição de autenticação (email/senha),
// valida as credenciais e, em caso de sucesso, gera o token JWT devolvido ao cliente.
// Diferente do JWTAuthorizationFilter (que valida o token nas requisições seguintes).
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Lê o corpo da requisição (email/senha) e delega a autenticação ao AuthenticationManager.
    // É chamado automaticamente pelo Spring Security quando a URL de login é acessada.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            CredenciaisDTO creds = new ObjectMapper()
                    .readValue(request.getInputStream(), CredenciaisDTO.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());

            Authentication auth = authenticationManager.authenticate(authToken);
            return auth;
        }
        catch (IOException e) {
            // Encapsula falha de leitura do corpo da requisição, já que o método não permite checked exceptions.
            throw new RuntimeException(e);
        }
    }

    // Chamado pelo Spring Security quando a autenticação dá certo: gera o JWT
    // e o devolve ao cliente no header Authorization, no formato esperado ("Bearer <token>").
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        //Pega o usuário logado
        String username = ((UserSS) auth.getPrincipal()).getUsername();

        //Gera o token
        String token = jwtUtil.generateToken(username);

        //Adiciona o token ao header da resposta
        response.addHeader("Authorization", "Bearer " + token);

        //Necessário para que o front-end receba o token
        response.addHeader("access-control-expose-headers", "Authorization");
    }
}
