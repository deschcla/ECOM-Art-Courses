package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.Produit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link Produit}.
 */
public interface ProduitService {
    /**
     * Save a produit.
     *
     * @param produit the entity to save.
     * @return the persisted entity.
     */
    Mono<Produit> save(Produit produit);

    /**
     * Updates a produit.
     *
     * @param produit the entity to update.
     * @return the persisted entity.
     */
    Mono<Produit> update(Produit produit);

    /**
     * Partially updates a produit.
     *
     * @param produit the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Produit> partialUpdate(Produit produit);

    /**
     * Get all the produits.
     *
     * @return the list of entities.
     */
    Flux<Produit> findAll();

    /**
     * Returns the number of produits available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" produit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Produit> findOne(Long id);

    /**
     * Delete the "id" produit.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
