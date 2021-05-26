package br.com.estudo.livrariaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"br.com.estudo.livrariaapi"})
public class LivrariaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivrariaApiApplication.class, args);
	}

}
