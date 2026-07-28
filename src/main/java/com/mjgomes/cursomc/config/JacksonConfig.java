package com.mjgomes.cursomc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjgomes.cursomc.domain.PagamentoComBoleto;
import com.mjgomes.cursomc.domain.PagamentoComCartao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

// Sem isso, o ObjectMapper não saberia serializar/desserializar corretamente os subtipos concretos
// de Pagamento (a hierarquia @JsonTypeInfo/@JsonTypeName sozinha não basta para o Spring Boot autoconfigurado).
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        // Configura o ObjectMapper para reconhecer as subtipos de Pagamento.
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
            @Override
            public void configure(ObjectMapper objectMapper) {
               objectMapper.registerSubtypes(PagamentoComCartao.class);
               objectMapper.registerSubtypes(PagamentoComBoleto.class);
                super.configure(objectMapper);
            }
        };
        return builder;
    }
}
