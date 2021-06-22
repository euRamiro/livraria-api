package br.com.estudo.livrariaapi.rest.controller.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDevolvidoDto {

	private Boolean devolvido;
}
