package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.SousCategorie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SousCategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousCategorieRepository extends ReactiveCrudRepository<SousCategorie, Long>, SousCategorieRepositoryInternal {
    @Query("SELECT * FROM sous_categorie entity WHERE entity.categorie_id = :id")
    Flux<SousCategorie> findByCategorie(Long id);

    @Query("SELECT * FROM sous_categorie entity WHERE entity.categorie_id IS NULL")
    Flux<SousCategorie> findAllWhereCategorieIsNull();

    @Override
    <S extends SousCategorie> Mono<S> save(S entity);

    @Override
    Flux<SousCategorie> findAll();

    @Override
    Mono<SousCategorie> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SousCategorieRepositoryInternal {
    <S extends SousCategorie> Mono<S> save(S entity);

    Flux<SousCategorie> findAllBy(Pageable pageable);

    Flux<SousCategorie> findAll();

    Mono<SousCategorie> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SousCategorie> findAllBy(Pageable pageable, Criteria criteria);
}
