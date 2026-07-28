package com.mjgomes.cursomc.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// Componente responsável por gerar e validar tokens JWT usados na autenticação stateless da API.
@Component
public class JWTUtil {

    // Segredo usado para assinar/verificar os tokens (HMAC), injetado via application.properties.
    @Value("${jwt.secret}")
    private String secret;

    // Tempo de expiração do token, em milissegundos.
    @Value("${jwt.expiration}")
    private long expiration;

    // Gera um token assinado (HS256+) contendo o username como subject e a data de expiração.
    public String generetaToken(String username){
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // Um token só é considerado válido se a assinatura conferir (getClaims não retornar null),
    // possuir subject e ainda não ter expirado.
    public boolean tokenValido(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (username != null && expirationDate != null && now.before(expirationDate)) {
                return true;
            }
        }
        return false;
    }

    // Extrai o username (subject) do token. Retorna null se o token for inválido/não puder ser lido.
    public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    // Faz o parse/verificação do token com a chave secreta. Qualquer falha de assinatura,
    // formato ou expiração é tratada aqui e vira null, para simplificar o uso pelos métodos acima.
    private Claims getClaims(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
