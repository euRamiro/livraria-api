package br.com.estudo.livrariaapi.api.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.persistence.repository.LivroRepository;
import br.com.estudo.livrariaapi.rest.service.LivroService;
import br.com.estudo.livrariaapi.rest.service.impl.LivroServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LivroServiceTest {

	LivroService livroService;
	@MockBean
	LivroRepository livroRepository;

	@BeforeEach
	public void setup() {
		this.livroService = new LivroServiceImpl(livroRepository);
	}

	@Test
	@DisplayName("Deve salvar um livro")
	public void deve_salvar_um_livro() {
		LivroEntity livro = LivroEntity.builder().titulo("os testes").autor("Testador").isbn("456").build();
		Mockito.when(livroRepository.save(livro))
				.thenReturn(LivroEntity.builder().id(22L).titulo("os testes").autor("Testador").isbn("456").build());

		LivroEntity livroSalvo = livroService.salvar(livro);

		assertThat(livroSalvo.getId()).isNotNull();
		assertThat(livroSalvo.getTitulo()).isEqualTo("os testes");
		assertThat(livroSalvo.getAutor()).isEqualTo("Testador");
		assertThat(livroSalvo.getIsbn()).isEqualTo("456");
	}

}
