package br.com.estudo.livrariaapi.rest.service;

import java.util.Optional;

import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;

public interface LivroService {

	public LivroEntity salvar(LivroEntity livro);

	public Optional<LivroEntity> buscarPorId(Long id);

}
