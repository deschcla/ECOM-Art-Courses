package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.LigneCommande;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the LigneCommande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneCommandeRepository extends ReactiveCrudRepository<LigneCommande, Long>, LigneCommandeRepositoryInternal {
    @Query("SELECT * FROM ligne_commande entity WHERE entity.produit_id = :id")
    Flux<LigneCommande> findByProduit(Long id);

    @Query("SELECT * FROM ligne_commande entity WHERE entity.produit_id IS NULL")
    Flux<LigneCommande> findAllWhereProduitIsNull();

    @Query("SELECT * FROM ligne_commande entity WHERE entity.commande_id = :id")
    Flux<LigneCommande> findByCommande(Long id);

    @Query("SELECT * FROM ligne_commande entity WHERE entity.commande_id IS NULL")
    Flux<LigneCommande> findAllWhereCommandeIsNull();

    @Override
    <S extends LigneCommande> Mono<S> save(S entity);

    @Override
    Flux<LigneCommande> findAll();

    @Override
    Mono<LigneCommande> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LigneCommandeRepositoryInternal {
    <S extends LigneCommande> Mono<S> save(S entity);

    Flux<LigneCommande> findAllBy(Pageable pageable);

    Flux<LigneCommande> findAll();

    Mono<LigneCommande> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<LigneCommande> findAllBy(Pageable pageable, Criteria criteria);
}
