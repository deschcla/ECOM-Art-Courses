package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.Commande;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Commande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandeRepository extends ReactiveCrudRepository<Commande, Long>, CommandeRepositoryInternal {
    @Override
    Mono<Commande> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Commande> findAllWithEagerRelationships();

    @Override
    Flux<Commande> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM commande entity JOIN rel_commande__produit joinTable ON entity.id = joinTable.produit_id WHERE joinTable.produit_id = :id"
    )
    Flux<Commande> findByProduit(Long id);

    @Query("SELECT * FROM commande entity WHERE entity.releve_facture_id = :id")
    Flux<Commande> findByReleveFacture(Long id);

    @Query("SELECT * FROM commande entity WHERE entity.releve_facture_id IS NULL")
    Flux<Commande> findAllWhereReleveFactureIsNull();

    @Query("SELECT * FROM commande entity WHERE entity.acheteur_id = :id")
    Flux<Commande> findByAcheteur(Long id);

    @Query("SELECT * FROM commande entity WHERE entity.acheteur_id IS NULL")
    Flux<Commande> findAllWhereAcheteurIsNull();

    @Override
    <S extends Commande> Mono<S> save(S entity);

    @Override
    Flux<Commande> findAll();

    @Override
    Mono<Commande> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CommandeRepositoryInternal {
    <S extends Commande> Mono<S> save(S entity);

    Flux<Commande> findAllBy(Pageable pageable);

    Flux<Commande> findAll();

    Mono<Commande> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Commande> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Commande> findOneWithEagerRelationships(Long id);

    Flux<Commande> findAllWithEagerRelationships();

    Flux<Commande> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
