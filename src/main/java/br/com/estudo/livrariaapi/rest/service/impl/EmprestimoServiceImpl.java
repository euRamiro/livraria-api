package br.com.estudo.livrariaapi.rest.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.estudo.livrariaapi.exception.model.RegraDeNegocioException;
import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.persistence.repository.EmprestimoRepository;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoFiltroDto;
import br.com.estudo.livrariaapi.rest.service.EmprestimoService;

@Service
public class EmprestimoServiceImpl implements EmprestimoService {

	@Autowired
	EmprestimoRepository emprestimoRepository;
	
	public EmprestimoServiceImpl(EmprestimoRepository emprestimoRepository) {
		this.emprestimoRepository = emprestimoRepository;
	}
	
	@Override
	public EmprestimoEntity salvar(EmprestimoEntity emprestimo) {
		if (emprestimoRepository.existsByLivroAndNotDevolvido(emprestimo.getLivro())) {
			throw new RegraDeNegocioException("Livro já emprestado.");
		}
		return emprestimoRepository.save(emprestimo);
	}

	@Override
	public Optional<EmprestimoEntity> buscarPorId(Long id) {
		return emprestimoRepository.findById(id);
	}

	@Override
	public EmprestimoEntity alterar(EmprestimoEntity emprestimo) {
		return emprestimoRepository.save(emprestimo);
	}

	@Override
	public Page<EmprestimoEntity> buscarPorIbsnOuCliente(EmprestimoFiltroDto filtroDto, Pageable pageRequest) {
		return emprestimoRepository.findByIbsnOrCliente(filtroDto.getIsbn(), filtroDto.getCliente(), pageRequest);
	}

}
