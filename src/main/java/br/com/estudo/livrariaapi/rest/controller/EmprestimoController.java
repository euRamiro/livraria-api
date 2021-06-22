package br.com.estudo.livrariaapi.rest.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import br.com.estudo.livrariaapi.rest.controller.domain.dto.mapper.EmprestimoMapper;
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
}
