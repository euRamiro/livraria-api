package br.com.estudo.livrariaapi.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.estudo.livrariaapi.exception.model.RegraDeNegocioException;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.LivroDto;
import br.com.estudo.livrariaapi.rest.service.LivroService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class LivroControllerTest {
	
	static String LIVRO_API = "/api/livros";	
	
	@Autowired
	MockMvc mock;		
	
	@MockBean
	LivroService livroService;
	
	@Test
	@DisplayName("deve salvar um livro com sucesso.")
	public void deve_salvar_um_livro() throws Exception {
		
		LivroDto livroDto = LivroDto.builder().titulo("Teste com tdd").autor("alguém").isbn("123").build();
		LivroEntity livroSalvo = LivroEntity.builder().id(100L).titulo("Teste com tdd").autor("alguém").isbn("123").build();
		
		BDDMockito
			.given(livroService.salvar(Mockito.any(LivroEntity.class))).willReturn(livroSalvo);
		
		String json =  new ObjectMapper().writeValueAsString(livroDto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(LIVRO_API)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(json);
		
		mock
			.perform(request)
			.andExpect(status().isCreated())
			;
	}
	
	@Test
	@DisplayName("deve lançar uma exception quando livro inválido.")
	public void deve_lancar_exception_quando_livro_invalido() throws Exception {
		
		String json =  new ObjectMapper().writeValueAsString(new LivroDto());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(LIVRO_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mock
			.perform(request)
			.andExpect(status().isUnprocessableEntity() )
			.andExpect(jsonPath("errors", Matchers.hasSize(3)))
			;
	}
	
	@Test
	@DisplayName("deve lançar uma exception quando cadastrar livro com isbn já cadastrado.")
	public void deve_lancar_exception_quando_cadastrar_livro_com_isbn_cadastrado() throws Exception {
		LivroDto livroDto = LivroDto.builder().titulo("Teste com tdd").autor("alguém").isbn("123").build();
				
		String json =  new ObjectMapper().writeValueAsString(livroDto);

		String mensagemErro = "Isbn já cadastrado.";
		BDDMockito
			.given(livroService.salvar(Mockito.any(LivroEntity.class)))
				.willThrow(new RegraDeNegocioException(mensagemErro));
		
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(LIVRO_API)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(json);
		
		mock
		.perform(request)
		.andExpect(status().isBadRequest() )
		.andExpect(jsonPath("message").value(mensagemErro))
		;
	}

}
