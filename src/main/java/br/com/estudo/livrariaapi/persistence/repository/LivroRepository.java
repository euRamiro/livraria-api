package br.com.estudo.livrariaapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;

public interface LivroRepository extends JpaRepository<LivroEntity, Long> {

	boolean existsByIsbn(String isbn);

}
