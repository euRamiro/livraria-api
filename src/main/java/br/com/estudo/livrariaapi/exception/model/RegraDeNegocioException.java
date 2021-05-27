package br.com.estudo.livrariaapi.exception.model;

public class RegraDeNegocioException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RegraDeNegocioException(String mensagem) {
		super(mensagem);
	}

	public RegraDeNegocioException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
