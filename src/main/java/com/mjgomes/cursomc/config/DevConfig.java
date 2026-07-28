package com.mjgomes.cursomc.config;

import com.mjgomes.cursomc.services.DBService;
import com.mjgomes.cursomc.services.EmailService;
import com.mjgomes.cursomc.services.SmtpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

// Configuração exclusiva do profile "dev": popula o banco com dados de exemplo (só quando o schema é
// recriado do zero) e usa o serviço de e-mail real (SmtpEmailService).
@Configuration
@Profile("dev")
public class DevConfig {

    @Autowired
    private DBService dbService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;

    // Só popula o banco quando o schema foi recriado (ddl-auto=create); em "update"/"validate" os dados
    // já existentes seriam duplicados a cada subida da aplicação.
    @Bean
    public boolean instantiateDatabase() throws ParseException {

        if (!"create".equals(strategy)) {
            return false;
        }

        dbService.instantiateTestDatabase();
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }
}
