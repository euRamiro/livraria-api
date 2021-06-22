package br.com.estudo.livrariaapi.rest.service;

import java.util.Optional;

import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;

public interface EmprestimoService {

	EmprestimoEntity salvar(EmprestimoEntity emprestimo);

	Optional<EmprestimoEntity> buscarPorId(Long id);

	EmprestimoEntity alterar(EmprestimoEntity emprestimo);

}
