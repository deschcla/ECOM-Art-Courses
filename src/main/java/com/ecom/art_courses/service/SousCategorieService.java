package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.SousCategorie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link SousCategorie}.
 */
public interface SousCategorieService {
    /**
     * Save a sousCategorie.
     *
     * @param sousCategorie the entity to save.
     * @return the persisted entity.
     */
    Mono<SousCategorie> save(SousCategorie sousCategorie);

    /**
     * Updates a sousCategorie.
     *
     * @param sousCategorie the entity to update.
     * @return the persisted entity.
     */
    Mono<SousCategorie> update(SousCategorie sousCategorie);

    /**
     * Partially updates a sousCategorie.
     *
     * @param sousCategorie the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SousCategorie> partialUpdate(SousCategorie sousCategorie);

    /**
     * Get all the sousCategories.
     *
     * @return the list of entities.
     */
    Flux<SousCategorie> findAll();

    /**
     * Returns the number of sousCategories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" sousCategorie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SousCategorie> findOne(Long id);

    /**
     * Delete the "id" sousCategorie.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
