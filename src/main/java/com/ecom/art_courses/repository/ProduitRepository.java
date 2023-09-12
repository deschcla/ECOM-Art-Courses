package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.Produit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Produit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProduitRepository extends ReactiveCrudRepository<Produit, Long>, ProduitRepositoryInternal {
    @Query("SELECT * FROM produit entity WHERE entity.souscategorie_id = :id")
    Flux<Produit> findBySouscategorie(Long id);

    @Query("SELECT * FROM produit entity WHERE entity.souscategorie_id IS NULL")
    Flux<Produit> findAllWhereSouscategorieIsNull();

    @Override
    <S extends Produit> Mono<S> save(S entity);

    @Override
    Flux<Produit> findAll();

    @Override
    Mono<Produit> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProduitRepositoryInternal {
    <S extends Produit> Mono<S> save(S entity);

    Flux<Produit> findAllBy(Pageable pageable);

    Flux<Produit> findAll();

    Mono<Produit> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Produit> findAllBy(Pageable pageable, Criteria criteria);
}
