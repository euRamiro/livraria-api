package br.com.estudo.livrariaapi.rest.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.estudo.livrariaapi.exception.model.ObjetoNaoEncontradoException;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.LivroDto;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.mapper.LivroMapper;
import br.com.estudo.livrariaapi.rest.service.LivroService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/livros")
public class LivroController {

	LivroService livroService;

	private LivroMapper livroMapper;

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

}
