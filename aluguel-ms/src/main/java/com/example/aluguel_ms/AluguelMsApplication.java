
package com.example.aluguel_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.aluguel_ms")
@EnableJpaRepositories(basePackages = "com.example.aluguel_ms")
public class AluguelMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AluguelMsApplication.class, args);
	}

}
