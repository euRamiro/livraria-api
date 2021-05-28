package br.com.estudo.livrariaapi.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

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
		LivroEntity livroSalvar = LivroEntity.builder().id(22L).titulo("os testes").autor("Testador").isbn(isbn).build();
		livroRepository.save(livroSalvar);
		
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
	
	@Test
	@DisplayName("deve buscar um livro por id")
	public void deve_buscar_um_livro_por_id() {				
		LivroEntity livroSalvar = LivroEntity.builder().titulo("Código limpo").autor("Bob").isbn("25258487").build();
		livroRepository.save(livroSalvar);
		
		Optional<LivroEntity> livroEncontrado = livroRepository.findById(livroSalvar.getId());
		
		assertThat(livroEncontrado.isPresent()).isTrue();		
	}
	
	@Test
	@DisplayName("deve editar um livro")
	public void deve_editar_um_livro() {
		LivroEntity livro = LivroEntity.builder().titulo("Design de Software").autor("Paulo Silveira").isbn("15487").build();
		
		LivroEntity livroSalvo = livroRepository.save(livro);
		
		assertThat(livroSalvo.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("deve deletar um livro")
	public void deve_deletar_um_livro() {
		LivroEntity livroSalvar = LivroEntity.builder().titulo("uml").autor("uml").isbn("99857").build();
		livroRepository.save(livroSalvar);		
		LivroEntity livroEncontrado = livroRepository.findById(livroSalvar.getId()).get();
		
		livroRepository.delete(livroEncontrado);
		Optional<LivroEntity> livroDeletado = livroRepository.findById(livroEncontrado.getId());
				
		assertThat(livroDeletado.isPresent()).isFalse();
	}
}