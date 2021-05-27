package br.com.estudo.livrariaapi.rest.controller.domain.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LivroDto {

	private Long id;
	@NotEmpty(message = "Título é obrigatório.")
	private String titulo;
	@NotEmpty(message = "Autor é obrigatório.")
	private String autor;
	@NotEmpty(message = "Isbn é obrigatório.")
	private String isbn;
}
