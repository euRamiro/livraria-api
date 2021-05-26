package br.com.estudo.livrariaapi.rest.service;

import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;

public interface LivroService {

	public LivroEntity salvar(LivroEntity livro);

}
