package br.com.estudo.livrariaapi.rest.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.estudo.livrariaapi.exception.model.RegraDeNegocioException;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.persistence.repository.LivroRepository;
import br.com.estudo.livrariaapi.rest.service.LivroService;

@Service
public class LivroServiceImpl implements LivroService {

	@Autowired
	LivroRepository livroRepository;

	public LivroServiceImpl(LivroRepository livroRepository) {
		this.livroRepository = livroRepository;
	}

	public LivroEntity salvar(LivroEntity livroEntity) {
		if (livroRepository.existsByIsbn(livroEntity.getIsbn())) {
			throw new RegraDeNegocioException("Isbn já cadastrado.");
		}
		return this.livroRepository.save(livroEntity);
	}
	
	public Optional<LivroEntity> buscarPorId(Long id) {		
		return this.livroRepository.findById(id);
	}

	public void deletar(LivroEntity livro) {
		if (livro == null || livro.getId() == null) {
			throw new IllegalArgumentException("Livro não pode ser vazio.");
		}
		this.livroRepository.delete(livro);	
	} 
	
	public void editar(LivroEntity livro) {
		if (livro == null || livro.getId() == null) {
			throw new IllegalArgumentException("Livro não pode ser vazio.");
		}
		this.livroRepository.save(livro);	
	} 
	

}
