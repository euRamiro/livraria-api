package br.com.estudo.livrariaapi.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;

public interface EmprestimoRepository extends JpaRepository<EmprestimoEntity, Long>{
	
	@Query(value = "select case when ( count(e.id) > 0 ) then true else false end "
			+ "from EmprestimoEntity e where e.livro = :livro and ( e.devolvido is null or  e.devolvido is not true )")
	boolean existsByLivroAndNotDevolvido(@Param("livro") LivroEntity livro);

	@Query(value= "select e from EmprestimoEntity as e join e.livro as l where l.isbn= ?1 or e.cliente = ?2")
	Page<EmprestimoEntity> findByIbsnOrCliente(
			@Param("ibsn") String isbn, 
			@Param("cliente") String cliente, 
			Pageable pageRequest);

}
