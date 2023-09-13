package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.CarteBancaire;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the CarteBancaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarteBancaireRepository extends ReactiveCrudRepository<CarteBancaire, Long>, CarteBancaireRepositoryInternal {
    @Query("SELECT * FROM carte_bancaire entity WHERE entity.commande_id = :id")
    Flux<CarteBancaire> findByCommande(Long id);

    @Query("SELECT * FROM carte_bancaire entity WHERE entity.commande_id IS NULL")
    Flux<CarteBancaire> findAllWhereCommandeIsNull();

    @Override
    <S extends CarteBancaire> Mono<S> save(S entity);

    @Override
    Flux<CarteBancaire> findAll();

    @Override
    Mono<CarteBancaire> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CarteBancaireRepositoryInternal {
    <S extends CarteBancaire> Mono<S> save(S entity);

    Flux<CarteBancaire> findAllBy(Pageable pageable);

    Flux<CarteBancaire> findAll();

    Mono<CarteBancaire> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CarteBancaire> findAllBy(Pageable pageable, Criteria criteria);
}
