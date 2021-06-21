package br.com.estudo.livrariaapi.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

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

import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.rest.controller.EmprestimoController;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoDto;
import br.com.estudo.livrariaapi.rest.service.EmprestimoService;
import br.com.estudo.livrariaapi.rest.service.LivroService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = EmprestimoController.class)
public class EmprestimoontrollerTest {
	
	static final String EMPRESTIMO_API = "/api/emprestimos";
	
	@Autowired
	MockMvc mock;
	
	@MockBean
	LivroService livroService;
	
	@MockBean
	EmprestimoService emprestimoService;
	
	@Test
	@DisplayName("deve realizar um empréstimo")
	public void createEmprestimoTest() throws Exception{
		
		EmprestimoDto dto = EmprestimoDto.builder()
				.isbn("123")
				.cliente("Lord")
				.build();
		String json = new ObjectMapper().writeValueAsString(dto);
		
		LivroEntity livro = LivroEntity.builder()
				.id(2L)
				.isbn("131516")
				.titulo("o poder do css")
				.autor("Maujor")
				.build();
		
		BDDMockito.given(livroService.buscarPorIsbn("123"))
				.willReturn(Optional.of(livro));
		EmprestimoEntity emprestimo = EmprestimoEntity.builder()
				.id(1L)
				.cliente("Lord")
				.livro(livro)
				.data(LocalDate.now())
				.build();
		BDDMockito.given(emprestimoService.salvar(Mockito.any(EmprestimoEntity.class)))
				.willReturn(emprestimo );
		
		MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
			.post(EMPRESTIMO_API)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json);
		
		mock
			.perform(request)
			.andExpect(status().isCreated())
			;
	}

}
