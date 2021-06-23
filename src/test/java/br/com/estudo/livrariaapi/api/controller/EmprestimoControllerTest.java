package br.com.estudo.livrariaapi.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.rest.controller.EmprestimoController;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoDevolvidoDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoFiltroDto;
import br.com.estudo.livrariaapi.rest.service.EmprestimoService;
import br.com.estudo.livrariaapi.rest.service.LivroService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = EmprestimoController.class)
@AutoConfigureMockMvc
public class EmprestimoControllerTest {
	
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
	
	@Test
	@DisplayName("deve retornar erro ao fazer empréstimo de livro inexistente")
	public void deve_retornar_erro_ao_fazer_emprestimo_de_livro_inexistente() throws Exception {
		EmprestimoDto dto = EmprestimoDto.builder()
				.isbn("123")
				.cliente("Lord")
				.build();
		String json = new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given(livroService.buscarPorIsbn("123"))
		.willReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
				.post(EMPRESTIMO_API)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
			
			mock
				.perform(request)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("error").value("Não existem dados."))
				.andExpect(jsonPath("message").value("Livro não encontrado."))
				;
	}

	@Test
	@DisplayName("deve retornar erro ao fazer empréstimo de livro emprestado")
	public void deve_retornar_erro_ao_fazer_emprestimo_de_livro_emprestado() throws Exception {
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
		
		BDDMockito.given(emprestimoService.salvar(Mockito.any(EmprestimoEntity.class)))
		.willThrow( new RegraDeNegocioException("Livro já emprestado.") );
		
		MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
				.post(EMPRESTIMO_API)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
			
			mock
				.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("error").value("Regra de negócio não atendida."))
				.andExpect(jsonPath("message").value("Livro já emprestado."))
				;
	}
	
	@Test
	@DisplayName("deve devolver um livro")
	public void deve_devolver_um_livro() throws Exception {
		EmprestimoDevolvidoDto dto = EmprestimoDevolvidoDto.builder().devolvido(true).build();
		EmprestimoEntity emprestimo = EmprestimoEntity.builder()
				.id(2L)
				.cliente("Lord")
				.data(LocalDate.now())
				.livro(LivroEntity.builder()
						.id(1L)
						.titulo("css power")
						.autor("Maujor")
						.isbn("159")
						.build())
				.build();
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given(emprestimoService.buscarPorId(Mockito.anyLong()))
			.willReturn(Optional.of(emprestimo));
				
		mock.perform(
				patch(EMPRESTIMO_API.concat("/1"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
			).andExpect(status().isOk());
				
		Mockito.verify(emprestimoService, Mockito.times(1)).alterar(emprestimo);
	}
	
	
	@Test
	@DisplayName("deve lançar uma exception ao devolver um livro inexistente")
	public void deve_lancar_uma_exception_ao_devolver_um_livro_inexistente() throws Exception {
		EmprestimoDevolvidoDto dto = EmprestimoDevolvidoDto.builder().devolvido(true).build();
				
		String json = new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given(emprestimoService.buscarPorId(Mockito.anyLong()))
		.willReturn(Optional.empty());
		
		mock.perform(
				patch(EMPRESTIMO_API.concat("/1"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
			).andExpect(status().isNotFound());
				
		Mockito.verify(emprestimoService, Mockito.never()).alterar(Mockito.any());
	}
	
	@Test
	@DisplayName("deve filtrar emprestimos")
	public void deve_filtrar_emprestimos() throws Exception{
		Long id = 2L;
		LivroEntity livro = LivroEntity.builder()
				.id(1L)
				.titulo("css power")
				.autor("Maujor")
				.isbn("159")
				.build();
		EmprestimoEntity emprestimo = EmprestimoEntity.builder()
				.id(id)
				.cliente("Lord")
				.data(LocalDate.now())
				.livro(livro)
				.build();
		
		BDDMockito.given(emprestimoService.buscarPorIbsnOuCliente(Mockito.any(EmprestimoFiltroDto.class), Mockito.any(Pageable.class)) )
			.willReturn( new PageImpl<EmprestimoEntity>(List.of(emprestimo), PageRequest.of(0,5), 1) );

		String queryString = String.format("?isbn=%S&cliente=%s&page=0&size=5",
				livro.getIsbn(), emprestimo.getCliente());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(EMPRESTIMO_API.concat(queryString))
				.accept(MediaType.APPLICATION_JSON)
				;
		
		mock.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("content", Matchers.hasSize(1)))
			.andExpect(jsonPath("totalElements").value(1))
			.andExpect(jsonPath("pageable.pageSize").value(5))
			.andExpect(jsonPath("pageable.pageNumber").value(0))
			;
	}
}
