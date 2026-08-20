package com.mjgomes.cursomc.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HeaderExposureFilter implements Filter{

    // Método chamado no início da aplicação
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Adiciona o header location para que o frontend possa acessar o id do recurso criado
        HttpServletResponse res = (HttpServletResponse) response;
        // O frontend não consegue ler o header location por padrão
        // Então precisamos adicioná-lo à lista de headers que podem ser acessados pelo frontend
        res.addHeader("access-control-expose-headers", "location");
        // Passa a requisição para o próximo filtro
        chain.doFilter(request, response);
    }

    // Método chamado no final da aplicação
    @Override
    public void destroy() {
    }

}
