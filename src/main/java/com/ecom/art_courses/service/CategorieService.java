package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.Categorie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link Categorie}.
 */
public interface CategorieService {
    /**
     * Save a categorie.
     *
     * @param categorie the entity to save.
     * @return the persisted entity.
     */
    Mono<Categorie> save(Categorie categorie);

    /**
     * Updates a categorie.
     *
     * @param categorie the entity to update.
     * @return the persisted entity.
     */
    Mono<Categorie> update(Categorie categorie);

    /**
     * Partially updates a categorie.
     *
     * @param categorie the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Categorie> partialUpdate(Categorie categorie);

    /**
     * Get all the categories.
     *
     * @return the list of entities.
     */
    Flux<Categorie> findAll();

    /**
     * Returns the number of categories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" categorie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Categorie> findOne(Long id);

    /**
     * Delete the "id" categorie.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
