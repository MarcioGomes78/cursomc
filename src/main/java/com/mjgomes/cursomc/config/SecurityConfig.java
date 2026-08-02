package com.mjgomes.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mjgomes.cursomc.security.JWTAuthenticationFilter;
import com.mjgomes.cursomc.security.JWTAuthorizationFilter;
import com.mjgomes.cursomc.security.JWTUtil;

// Configuração central de segurança da aplicação: autenticação stateless via JWT,
// regras de acesso por endpoint, CORS e registro dos filtros JWTAuthenticationFilter/JWTAuthorizationFilter.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Environment env;

    @Autowired
    private JWTUtil jwtUtil;

    // Endpoints liberados independente do verbo HTTP.
    private static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**"
    };

    // Endpoints liberados apenas para leitura (GET); demais verbos exigem autenticação.
    private static final String[] PUBLIC_MATCHERS_GET = {
            "/produtos/**",
            "/categorias/**"
    };

    // Endpoints liberados apenas para escrita (POST), como o cadastro de clientes.
    private static final String[] PUBLIC_MATCHERS_POST = {
            "/clientes",
            "/auth/forgot"
    };

    public SecurityConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Desabilita a proteção de frames apenas no profile de teste, para permitir
        // que o console do H2 (que usa <frame>) seja acessado no navegador.
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        }
        // Autenticação via banco (UserDetailsService) comparando a senha com BCrypt.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        AuthenticationManager authenticationManager = new ProviderManager(authProvider);

        http.cors(withDefaults());
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated()
        )
                // CSRF desabilitado porque a API é stateless (sem sessão/cookies) e usa JWT no header Authorization.
                .csrf(csrf -> csrf.disable());
        http.authenticationManager(authenticationManager);
        // JWTAuthenticationFilter cuida do login (gera o token); JWTAuthorizationFilter valida
        // o token nas demais requisições e popula o SecurityContext.
        http.addFilter(new JWTAuthenticationFilter(authenticationManager, jwtUtil));
        http.addFilter(new JWTAuthorizationFilter(authenticationManager, jwtUtil, userDetailsService));
        // Sem sessão no servidor: cada requisição precisa se autenticar via token JWT.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();

    }

    // Libera CORS para todas as origens/rotas com as configurações padrão do Spring.
    @Bean
    @SuppressWarnings("unused")
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
