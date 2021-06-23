package br.com.estudo.livrariaapi.rest.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoFiltroDto;

public interface EmprestimoService {

	public EmprestimoEntity salvar(EmprestimoEntity emprestimo);

	public Optional<EmprestimoEntity> buscarPorId(Long id);

	public EmprestimoEntity alterar(EmprestimoEntity emprestimo);

	public Page<EmprestimoEntity> buscarPorIbsnOuCliente(EmprestimoFiltroDto filtroDto, Pageable pageRequest);

}
