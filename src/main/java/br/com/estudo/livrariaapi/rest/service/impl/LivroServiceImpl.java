package br.com.estudo.livrariaapi.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return livroRepository.save(livroEntity);
	}

}
