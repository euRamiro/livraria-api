package br.com.estudo.livrariaapi.rest.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;

public interface LivroService {

	public LivroEntity salvar(LivroEntity livro);

	public Optional<LivroEntity> buscarPorId(Long id);

	public void deletar(LivroEntity livro);

	public LivroEntity editar(LivroEntity livro);

	public Page<LivroEntity> buscarPorTituloAutor(LivroEntity filtro, Pageable pageable);
	
	

}
