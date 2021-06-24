package br.com.estudo.livrariaapi.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.estudo.livrariaapi.exception.model.RegraDeNegocioException;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.rest.controller.LivroController;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.LivroDto;
import br.com.estudo.livrariaapi.rest.service.EmprestimoService;
import br.com.estudo.livrariaapi.rest.service.LivroService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LivroController.class)
@AutoConfigureMockMvc
public class LivroControllerTest {

	static String LIVRO_API = "/api/livros";

	@Autowired
	MockMvc mock;
	
	@MockBean
	LivroService livroService;
	@MockBean
	EmprestimoService emmprestimoService;
		
	@Test
	@DisplayName("deve salvar um livro com sucesso.")
	public void deve_salvar_um_livro() throws Exception {

		LivroDto livroDto = LivroDto.builder().titulo("Teste com tdd").autor("alguém").isbn("123").build();
		LivroEntity livroSalvo = LivroEntity.builder().id(100L).titulo("Teste com tdd").autor("alguém").isbn("123")
				.build();

		BDDMockito.given(livroService.salvar(Mockito.any(LivroEntity.class))).willReturn(livroSalvo);

		String json = new ObjectMapper().writeValueAsString(livroDto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LIVRO_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mock.perform(request).andExpect(status().isCreated());
	}

	@Test
	@DisplayName("deve lançar uma exception quando livro inválido.")
	public void deve_lancar_exception_quando_livro_invalido() throws Exception {

		String json = new ObjectMapper().writeValueAsString(new LivroDto());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LIVRO_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mock.perform(request).andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("errors", Matchers.hasSize(3)));
	}

	@Test
	@DisplayName("deve lançar uma exception quando cadastrar livro com isbn já cadastrado.")
	public void deve_lancar_exception_quando_cadastrar_livro_com_isbn_cadastrado() throws Exception {
		LivroDto livroDto = LivroDto.builder().titulo("Teste com tdd").autor("alguém").isbn("123").build();

		String json = new ObjectMapper().writeValueAsString(livroDto);

		String mensagemErro = "Isbn já cadastrado.";
		BDDMockito.given(livroService.salvar(Mockito.any(LivroEntity.class)))
				.willThrow(new RegraDeNegocioException(mensagemErro));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LIVRO_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mock.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("message").value(mensagemErro));
	}

	@Test
	@DisplayName("deve buscar um livro por id")
	public void deve_buscar_um_livro_por_id() throws Exception {
		Long id = 100L;
		LivroEntity livroSalvo = LivroEntity.builder().id(100L).titulo("Teste com tdd").autor("alguém").isbn("123").build();

		BDDMockito.given(livroService.buscarPorId(id)).willReturn(Optional.of(livroSalvo));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(LIVRO_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		mock
			.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("id").value(id))
			.andExpect(jsonPath("titulo").value(livroSalvo.getTitulo()))
			.andExpect(jsonPath("autor").value(livroSalvo.getAutor()))
			.andExpect(jsonPath("isbn").value(livroSalvo.getIsbn()))
			;
	}

	@Test
	@DisplayName("deve lançar ObjetoNaoEncontrado quando buscar um livro inexistente ")
	public void deve_lancar_ObjetoNaoEncontraException_quando_buscar_um_livro_inexistente() throws Exception {
		
		BDDMockito.given(livroService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(LIVRO_API.concat("/" + 10))
				.accept(MediaType.APPLICATION_JSON);

		mock
			.perform(request)
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("message").value("Livro não encontrado."))
			;
	}
	
	@Test
	@DisplayName("deve deletar um livro")
	public void deve_deletar_um_livro() throws Exception {
		Long id = 105L;
		
		BDDMockito.given(livroService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(LivroEntity.builder().id(id).build()));
		
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(LIVRO_API.concat("/"+id));
		
		mock
			.perform(request)
			.andExpect(status().isNoContent())
			;
	}
	
	@Test
	@DisplayName("deve lancar ObjetoNaoEncontradoException quando deletar livro inexistente")
	public void deve_lancar_ObjetoNaoEncontradoException_quando_deletar_livro_inexistente() throws Exception {
		
		BDDMockito.given(livroService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());
		
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(LIVRO_API.concat("/"+10));
		
		mock
		.perform(request)
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("message").value("Livro não encontrado."))
		;
	}
	
	@Test
	@DisplayName("deve editar um livro")
	public void deve_editar_um_livro() throws Exception {
		Long id = 80L;
		LivroEntity livroSalvo = LivroEntity.builder().id(id).titulo("Teste com tdd").autor("alguém").isbn("123").build();
		BDDMockito.given(livroService.buscarPorId(id)).willReturn(Optional.of(livroSalvo));
		
		
		LivroEntity livroEditado = LivroEntity.builder().id(id).titulo("Springboot na prática").autor("Spring").isbn("123").build();				
		BDDMockito.given(livroService.salvar(livroEditado)).willReturn(livroEditado);
		
		String json = new ObjectMapper().writeValueAsString(livroEditado);		
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(LIVRO_API.concat("/"+id))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mock
			.perform(request)
			.andExpect(status().isCreated());
	}
	
	@Test
	@DisplayName("deve lançar ObjetoNaoEncontradoException quando editar um livro inexistente")
	public void deve_lancar_ObjetoNaoEncontradoException_quando_editar_livro_inexistente() throws Exception{				

		String json = new ObjectMapper().writeValueAsString(LivroDto.builder().id(6L).titulo("Springboot na prática").autor("Spring").isbn("741").build());
		
		BDDMockito.given(livroService.buscarPorId(Mockito.anyLong()))
			.willReturn(Optional.empty());
		
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(LIVRO_API.concat("/"+100))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mock
			.perform(request)
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("message").value("Livro não encontrado."))
			;
	}
	
	@Test
	@DisplayName("deve buscar livros")
	public void deve_buscar_livros() throws Exception {
		Long id = 38L;
		LivroEntity livro = LivroEntity.builder().id(id).titulo("React na prática").autor("Moujor").isbn("121589").build();
		List<LivroEntity> lista = new ArrayList<>();
		lista.add(livro);
				
		BDDMockito
			.given(livroService.buscarPorTituloAutor(Mockito.any(LivroEntity.class), Mockito.any(Pageable.class)))
			.willReturn(new PageImpl<LivroEntity>(lista, PageRequest.of(1, 50), 1));		
		
		String queryString = String.format("?titulo=%s&autor=%s&page=0&size=50", 
				livro.getTitulo(), livro.getAutor());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(LIVRO_API.concat(queryString))
				.accept(MediaType.APPLICATION_JSON);
					
		mock
			.perform(request)
			.andExpect( status().isOk())
			.andExpect(jsonPath("content", Matchers.hasSize(1)))
			.andExpect(jsonPath("totalElements").value(1))
			.andExpect(jsonPath("pageable.pageSize").value(50))
			.andExpect(jsonPath("pageable.pageNumber").value(0))
			;
	}
	
}
