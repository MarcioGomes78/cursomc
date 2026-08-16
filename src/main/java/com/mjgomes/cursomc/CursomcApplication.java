package com.mjgomes.cursomc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ponto de entrada da aplicação. Implementa CommandLineRunner apenas como gancho para lógica de
// startup futura; hoje o run() está vazio (a carga de dados de exemplo acontece via DevConfig/TestConfig).
@SpringBootApplication
public class CursomcApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
