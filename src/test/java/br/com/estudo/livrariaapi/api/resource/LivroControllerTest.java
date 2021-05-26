package br.com.estudo.livrariaapi.api.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}
