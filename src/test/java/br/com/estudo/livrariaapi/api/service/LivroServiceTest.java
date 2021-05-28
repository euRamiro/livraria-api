package br.com.estudo.livrariaapi.api.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.estudo.livrariaapi.exception.model.RegraDeNegocioException;
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

	@Test
	@DisplayName("deve lançar uma exception quando cadastrar livro com isbn já cadastrado.")
	public void deve_lancar_exception_quando_cadastrar_livro_com_isbn_cadastrado() throws Exception {
		LivroEntity livro = LivroEntity.builder().titulo("os testes").autor("Testador").isbn("456").build();

		Mockito.when(livroRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);
		Throwable exception = Assertions.catchThrowable(() -> livroService.salvar(livro));

		assertThat(exception).isInstanceOf(RegraDeNegocioException.class).hasMessage("Isbn já cadastrado.");
		Mockito.verify(livroRepository, Mockito.never()).save(livro);
	}

	@Test
	@DisplayName("deve buscar um livro por id")
	public void deve_buscar_um_livro_por_id() throws Exception {
		Long id = 55L;
		LivroEntity livroSalvo = LivroEntity.builder().id(id).titulo("Springboot na prática").autor("Spring").isbn("741").build();
		Mockito.when(livroRepository.findById(id)).thenReturn(Optional.of(livroSalvo));
		
		Optional<LivroEntity> livroEncontrado = livroService.buscarPorId(id);
		
		assertThat(livroEncontrado.isPresent()).isTrue();
		assertThat(livroSalvo.getTitulo()).isEqualTo(livroEncontrado.get().getTitulo());
		assertThat(livroSalvo.getAutor()).isEqualTo(livroEncontrado.get().getAutor());
		assertThat(livroSalvo.getIsbn()).isEqualTo(livroEncontrado.get().getIsbn());
	}
	
	@Test
	@DisplayName("deve retornar vazio quando buscar um livro inexistente ")
	public void deve_retornar_vazio_quando_buscar_um_livro_inexistente() throws Exception {
		Long id = 26L;		
		Mockito.when(livroRepository.findById(id)).thenReturn(Optional.empty());
		
		Optional<LivroEntity> livroEncontrado = livroService.buscarPorId(id);
		
		assertThat(livroEncontrado.isPresent()).isFalse();
		Mockito.verify(livroRepository, Mockito.times(1)).findById(id);
	}
	
	@Test
	@DisplayName("deve deletar um livro")
	public void deve_deletar_um_livro() {
		 LivroEntity livro = LivroEntity.builder().id(12L).build();
		 
		 org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> livroService.deletar(livro));
		 
		 Mockito.verify(livroRepository, Mockito.times(1)).delete(livro);
	}
	
	@Test
	@DisplayName("deve lançar Exception ao deletar livro inexistente")
	public void deve_lancar_Exception_deletar_licro_inesistente() {
		 LivroEntity livro = new LivroEntity();
		 
		 org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> livroService.deletar(livro));
		 
		 Mockito.verify(livroRepository, Mockito.never()).delete(livro);
	}
	
	@Test
	@DisplayName("deve lançar Exception ao editar livro inexistente")
	public void deve_lancar_Exception_editar_licro_inesistente() {
		 LivroEntity livro = new LivroEntity();
		 
		 org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> livroService.editar(livro));
		 
		 Mockito.verify(livroRepository, Mockito.never()).save(livro);
	}
	
	@Test
	@DisplayName("deve editar livro inexistente")
	public void deve_editar_licro_inesistente() {
		Long id = 15L;
		LivroEntity livroSalvo = LivroEntity.builder().id(id).build();
		 
		LivroEntity dadosParaEditar = LivroEntity.builder().id(id).titulo("Springboot na prática").autor("Spring").isbn("741").build();
		
		Mockito.when(livroRepository.save(livroSalvo)).thenReturn(dadosParaEditar);
		
		LivroEntity livro = livroService.editar(livroSalvo);
		
		assertThat(livro.getId()).isEqualTo(id);
		assertThat(livro.getTitulo()).isEqualTo(dadosParaEditar.getTitulo());
		assertThat(livro.getAutor()).isEqualTo(dadosParaEditar.getAutor());
		assertThat(livro.getIsbn()).isEqualTo(dadosParaEditar.getIsbn());
	}
	
	@Test
	@DisplayName("deve buscar por título e autor")
	public void deve_buscar_por_titulo_e_autor() {
		LivroEntity livro = LivroEntity.builder().titulo("Refatorando").autor("João").isbn("135984").build();
		
		PageRequest pageRequest = PageRequest.of(0, 10);
				
		List<LivroEntity> lista = new ArrayList<>();
		lista.add(livro);
		Page<LivroEntity> page = new PageImpl<LivroEntity>(lista, pageRequest, 1);
		Mockito
			.when( livroRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
			.thenReturn(page);
		
		Page<LivroEntity> resultado = livroService.buscarPorTituloAutor(livro, pageRequest);
		
		assertThat(resultado.getTotalElements()).isEqualTo(1);
		assertThat(resultado.getContent()).isEqualTo(lista);
		assertThat(resultado.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(resultado.getPageable().getPageSize()).isEqualTo(10);
	}
}
