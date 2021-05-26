package br.com.estudo.livrariaapi.rest.controller.domain.dto;

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
	private String titulo;
	private String autor;
	private String isbn;
}
