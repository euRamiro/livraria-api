package br.com.estudo.livrariaapi.rest.controller.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDto {

	private Long id;
	
	@NotEmpty(message = "Isbn não informado.")
	private String isbn;
	
	@NotEmpty(message = "Cliente não informado.")
	private String cliente;
	
	@NotEmpty(message = "E-mail não informado.")
	@Email
	private String emailCliente;
		
	private LivroDto livro;
}
