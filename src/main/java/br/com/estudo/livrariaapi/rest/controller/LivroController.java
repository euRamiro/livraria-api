package br.com.estudo.livrariaapi.rest.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.estudo.livrariaapi.exception.model.ObjetoNaoEncontradoException;
import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.LivroDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.mapper.EmprestimoMapper;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.mapper.LivroMapper;
import br.com.estudo.livrariaapi.rest.service.EmprestimoService;
import br.com.estudo.livrariaapi.rest.service.LivroService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/livros")
public class LivroController {
	
	LivroService livroService;
	EmprestimoService emprestimoService;
	
	private LivroMapper livroMapper;
	private EmprestimoMapper emprestimoMapper;

	@PostMapping
	public ResponseEntity<LivroDto> salvar(@RequestBody @Validated LivroDto livroDto) {		
		LivroEntity livroSalvo = livroService.salvar(livroMapper.toEntity(livroDto));
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(livroMapper.toDto(livroSalvo))
				.toUri();

		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("{id}")	
	public LivroDto buscarPorId(@PathVariable Long id){
		return livroService
					.buscarPorId(id)
					.map(livro -> livroMapper.toDto(livro))
					.orElseThrow(() -> new ObjetoNaoEncontradoException("Livro não encontrado."))
					;
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id) {
		LivroEntity livroDeletar = livroService.buscarPorId(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Livro não encontrado."));
		
		livroService.deletar(livroDeletar);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("{id}")
	public ResponseEntity<LivroDto> editar(@PathVariable Long id, @RequestBody LivroDto livroDto) {
		LivroEntity livroEditar = livroService.buscarPorId(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Livro não encontrado."));
		
		livroEditar.setTitulo(livroDto.getTitulo());
		livroEditar.setAutor(livroDto.getAutor());
		LivroDto livroSalvo = livroMapper.toDto(livroService.salvar(livroEditar));
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(livroSalvo).toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	public Page<LivroDto> buscarPorTituloAutor(LivroDto dto, Pageable pageable){
		LivroEntity livroBuscar = livroMapper.toEntity(dto);
		Page<LivroEntity> resultado = livroService.buscarPorTituloAutor(livroBuscar, pageable);
		List<LivroDto> lista = resultado
					.getContent()
					.stream()
					.map(livro -> livroMapper.toDto(livro))
					.collect(Collectors.toList());
		return new PageImpl<LivroDto>(lista, pageable, resultado.getNumberOfElements());
	}
	
	@GetMapping("{id}/emprestimos")
	public Page<EmprestimoDto> emprestimosPorLivro( @PathVariable Long id, Pageable pageable) {
		LivroEntity livroEncontrado = livroService.buscarPorId(id).orElseThrow( () -> new ObjetoNaoEncontradoException("Livro não encontrado."));
		Page<EmprestimoEntity> emprestimosAllPages = emprestimoService.buscarEmprestimosPorLivro(livroEncontrado, pageable);
		List<EmprestimoDto> emprestimosList = emprestimosAllPages.getContent()
				.stream()
				.map(emprestimo -> {
						LivroEntity livroEmprestimo = emprestimo.getLivro();
						LivroDto livroDto = livroMapper.toDto(livroEmprestimo);
						EmprestimoDto emprestimoDto = emprestimoMapper.toDto(emprestimo);
						emprestimoDto.setLivro(livroDto);						
						return emprestimoDto;
					}						
				).collect(Collectors.toList())
				;
				
		return new PageImpl<EmprestimoDto>( emprestimosList, pageable, emprestimosAllPages.getTotalElements());
	}
}
