package br.com.estudo.livrariaapi.rest.controller.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmprestimoFiltroDto {

	private String isbn;
	private String cliente;
}
