package br.com.estudo.livrariaapi.rest.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.estudo.livrariaapi.exception.model.ObjetoNaoEncontradoException;
import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoDevolvidoDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoFiltroDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.LivroDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.mapper.EmprestimoMapper;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.mapper.LivroMapper;
import br.com.estudo.livrariaapi.rest.service.EmprestimoService;
import br.com.estudo.livrariaapi.rest.service.LivroService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {
	
	EmprestimoService emprestimoService;	
	LivroService livroService;
	
	private EmprestimoMapper emprestimoMapper;
	private LivroMapper livroMapper;
	
	@PostMapping
	public ResponseEntity<EmprestimoDto> salvar(@RequestBody @Validated EmprestimoDto emprestimoDto) {		
		LivroEntity livroEncontrado = livroService.buscarPorIsbn(emprestimoDto.getIsbn())
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Livro não encontrado."));
		EmprestimoEntity emprestimoSalvar =emprestimoMapper.toEntity(emprestimoDto);
		emprestimoSalvar.setLivro(livroEncontrado);		
		
		EmprestimoEntity emprestimoSalvo = emprestimoService.salvar(emprestimoSalvar);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(emprestimoMapper.toDto(emprestimoSalvo))
				.toUri();

		return ResponseEntity.created(uri).build();
	}
	
	@PatchMapping("{id}")
	public void devolverLivro( @PathVariable Long id, @RequestBody EmprestimoDevolvidoDto dto) {
		EmprestimoEntity emprestimo = emprestimoService.buscarPorId(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Emprestino não encontrado."));
		emprestimo.setDevolvido(dto.getDevolvido());
		emprestimoService.alterar(emprestimo);
	}
	
	@GetMapping
	public Page<EmprestimoDto> buscarPorIsbnOuCliente(EmprestimoFiltroDto filtroDto, Pageable pageable){
		
		Page<EmprestimoEntity> resultado = emprestimoService.buscarPorIbsnOuCliente(filtroDto, pageable);
		List<EmprestimoDto> lista = resultado
					.getContent()
					.stream()
					.map(emprestimo -> {
						LivroEntity livro = emprestimo.getLivro();
						LivroDto livroDto = livroMapper.toDto(livro);
						EmprestimoDto emprestimoDto = emprestimoMapper.toDto(emprestimo);
						emprestimoDto.setLivro(livroDto);					
						return emprestimoDto;
					}).collect(Collectors.toList());
		return new PageImpl<EmprestimoDto>(lista, pageable, resultado.getNumberOfElements());
	}	
	
}
