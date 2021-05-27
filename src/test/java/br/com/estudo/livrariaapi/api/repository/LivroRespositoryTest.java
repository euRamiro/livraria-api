package br.com.estudo.livrariaapi.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.persistence.repository.LivroRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LivroRespositoryTest {

	@Autowired
	LivroRepository livroRepository;
		
	@Test
	@DisplayName("deve retornar true quando existir um livro cadastrado com isbn")
	public void deve_retornar_true_quando_existir_livro_cadastrado_com_isbn() {
		String isbn = "123";
		livroRepository.save(LivroEntity.builder().id(22L).titulo("os testes").autor("Testador").isbn(isbn).build());
		
		boolean existe = livroRepository.existsByIsbn(isbn);
		
		assertThat(existe).isTrue();
	}
	
	@Test
	@DisplayName("deve retornar false quando não existir um livro cadastrado com isbn")
	public void deve_retornar_false_quando_nao_existir_livro_cadastrado_com_isbn() {
		String isbn = "123";		
		
		boolean existe = livroRepository.existsByIsbn(isbn);
		
		assertThat(existe).isFalse();
	}
}