package br.com.estudo.livrariaapi.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.persistence.repository.EmprestimoRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class EmprestimoRespositoryTest {

	@Autowired
	EmprestimoRepository emprestimoRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("deve verificar se existe emprestimo não devolvido para livro")
	public void existsByLivroAndNotDevolvido() {
		
		LivroEntity livro = LivroEntity.builder()
				.titulo("css power")
				.autor("Maujor")
				.isbn("159")
				.build();
		entityManager.persist(livro);
		
		EmprestimoEntity emprestimo = EmprestimoEntity.builder()
				.livro(livro)
				.cliente("Lord")
				.data(LocalDate.now())
				.build();
		entityManager.persist(emprestimo);
		
		boolean exists = emprestimoRepository.existsByLivroAndNotDevolvido(livro);
		
		assertThat(exists).isTrue();		
	}
	
	
}
