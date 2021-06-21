package br.com.estudo.livrariaapi.rest.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
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
		EmprestimoEntity emprestimoSalvo = emprestimoService.salvar(emprestimoMapper.toEntity(emprestimoDto));
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(emprestimoMapper.toDto(emprestimoSalvo))
				.toUri();

		return ResponseEntity.created(uri).build();
	}
}
