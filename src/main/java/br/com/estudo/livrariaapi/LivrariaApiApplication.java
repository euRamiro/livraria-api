package br.com.estudo.livrariaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({ "br.com.estudo.livrariaapi" })
@EnableScheduling
public class LivrariaApiApplication {
	
//	@Autowired
//	private EmailService emailService;
	
//	@Bean
//	public CommandLineRunner runner() {
//		return args -> {
//			List<String> emails = Arrays.asList("65c5b8add4-ea1b3c@inbox.mailtrap.io");
//			emailService.enviarEmails("teste", emails);
//			System.out.println("email enviado.");
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(LivrariaApiApplication.class, args);
	}

}
