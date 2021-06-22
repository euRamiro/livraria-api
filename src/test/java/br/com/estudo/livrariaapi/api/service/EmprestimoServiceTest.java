package br.com.estudo.livrariaapi.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.estudo.livrariaapi.exception.model.RegraDeNegocioException;
import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.persistence.repository.EmprestimoRepository;
import br.com.estudo.livrariaapi.rest.service.EmprestimoService;
import br.com.estudo.livrariaapi.rest.service.impl.EmprestimoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EmprestimoServiceTest {
	EmprestimoService emprestimoService;
	
	@MockBean
	EmprestimoRepository emprestimoRepository;

	@BeforeEach
	public void setup() {
		this.emprestimoService = new EmprestimoServiceImpl(emprestimoRepository);
	}
	
	@Test
	@DisplayName("deve salvar um emprestimo")
	public void deve_salvar_um_emprestimo() {
		LivroEntity livro = LivroEntity.builder()
				.id(1L)
				.autor("Maujor")
				.titulo("css power")
				.isbn("159")
				.build();
		String cliente = "Lord";
		
		EmprestimoEntity emprestimoNovo = EmprestimoEntity.builder()				
				.livro(livro)
				.cliente(cliente)
				.data(LocalDate.now())
				.devolvido(false)
				.build();
		
		EmprestimoEntity emprestimoComId = EmprestimoEntity.builder()
				.id(5L)
				.livro(livro)
				.cliente(cliente)
				.data(LocalDate.now())
				.devolvido(false)
				.build();
		
		when(emprestimoRepository.existsByLivroAndNotDevolvido(livro)).thenReturn(false);
		when(emprestimoRepository.save(emprestimoNovo)).thenReturn(emprestimoComId);
		
		EmprestimoEntity emprestimoSalvo = emprestimoService.salvar(emprestimoNovo);
		
		assertThat(emprestimoSalvo.getId()).isEqualTo(emprestimoComId.getId());
		assertThat(emprestimoSalvo.getCliente()).isEqualTo(cliente);
		assertThat(emprestimoSalvo.getData()).isEqualTo(emprestimoComId.getData());
		assertThat(emprestimoSalvo.getLivro()).isEqualTo(emprestimoComId.getLivro());
		
	}
	
	@Test
	@DisplayName("deve lançar exception ao salvar um emprestimo com livro já emprestado")
	public void deve_lancar_exception_ao_salvar_um_emprestimo_com_livro_ja_emprestado() {
		LivroEntity livro = LivroEntity.builder()
				.id(1L)
				.autor("Maujor")
				.titulo("css power")
				.isbn("159")
				.build();
		String cliente = "Lord";
		
		EmprestimoEntity emprestimoNovo = EmprestimoEntity.builder()				
				.livro(livro)
				.cliente(cliente)
				.data(LocalDate.now())
				.devolvido(false)
				.build();
	
		when(emprestimoRepository.existsByLivroAndNotDevolvido(livro)).thenReturn(true);
		
		Throwable exception =  catchThrowable(() -> emprestimoService.salvar(emprestimoNovo));
		
		assertThat(exception)
			.isInstanceOf(RegraDeNegocioException.class)
			.hasMessage("Livro já emprestado.");
		verify(emprestimoRepository, never()).save(emprestimoNovo);
	}
	
	@Test
	@DisplayName("deve buscar um emprestimo por id")
	public void deve_buscar_um_emprestimo_por_id() {
		Long id = 3L;
		LivroEntity livro = LivroEntity.builder()
				.id(1L)
				.autor("Maujor")
				.titulo("css power")
				.isbn("159")
				.build();
		String cliente = "Lord";
		
		EmprestimoEntity emprestimoSalvo = EmprestimoEntity.builder()				
				.id(id)
				.livro(livro)
				.cliente(cliente)
				.data(LocalDate.now())
				.devolvido(false)
				.build();
		
		when(emprestimoRepository.findById(id)).thenReturn(Optional.of(emprestimoSalvo));
		
		EmprestimoEntity emprestimoEncontrado = emprestimoService.buscarPorId(id).get();
		
		assertThat(emprestimoEncontrado.getId()).isEqualTo(id);
		assertThat(emprestimoEncontrado.getLivro()).isEqualTo(emprestimoSalvo.getLivro());
		assertThat(emprestimoEncontrado.getCliente()).isEqualTo(emprestimoSalvo.getCliente());
		assertThat(emprestimoEncontrado.getData()).isEqualTo(emprestimoSalvo.getData());
		
		verify(emprestimoRepository).findById(id);
	}
	
	@Test
	@DisplayName("deve alterar um emprestimo")
	public void deve_altera_um_emprestimo() {
		Long id = 5L;
		LivroEntity livro = LivroEntity.builder()
				.id(1L)
				.autor("Maujor")
				.titulo("css power")
				.isbn("159")
				.build();
		String cliente = "Lord";
		
		EmprestimoEntity emprestimo = EmprestimoEntity.builder()				
				.id(id)
				.livro(livro)
				.cliente(cliente)
				.data(LocalDate.now())
				.devolvido(true)
				.build();
		
		when(emprestimoRepository.save(Mockito.any())).thenReturn(emprestimo);
		
		EmprestimoEntity emprestimoAlterado = emprestimoService.alterar(emprestimo);
		
		assertThat(emprestimoAlterado.getId()).isEqualTo(id);
		assertThat(emprestimoAlterado.getLivro()).isEqualTo(emprestimo.getLivro());
		assertThat(emprestimoAlterado.getCliente()).isEqualTo(emprestimo.getCliente());
		assertThat(emprestimoAlterado.getData()).isEqualTo(emprestimo.getData());
		
		verify(emprestimoRepository).save(Mockito.any());
	}
}
