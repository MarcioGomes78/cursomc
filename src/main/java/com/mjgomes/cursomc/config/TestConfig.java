package com.mjgomes.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mjgomes.cursomc.services.DBService;
import com.mjgomes.cursomc.services.EmailService;
import com.mjgomes.cursomc.services.MockEmailService;

// Configuração exclusiva do profile "test": sempre popula o H2 em memória com dados de exemplo
// (recriado do zero a cada subida) e usa um serviço de e-mail simulado (MockEmailService).
@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    // Diferente do DevConfig, aqui não há checagem de estratégia: o H2 em memória é sempre novo.
    @Bean
    public boolean instantiateDatabase() throws ParseException {

        dbService.instantiateTestDatabase();
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }
}
