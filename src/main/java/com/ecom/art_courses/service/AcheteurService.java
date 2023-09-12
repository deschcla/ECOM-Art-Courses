package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.Acheteur;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link Acheteur}.
 */
public interface AcheteurService {
    /**
     * Save a acheteur.
     *
     * @param acheteur the entity to save.
     * @return the persisted entity.
     */
    Mono<Acheteur> save(Acheteur acheteur);

    /**
     * Updates a acheteur.
     *
     * @param acheteur the entity to update.
     * @return the persisted entity.
     */
    Mono<Acheteur> update(Acheteur acheteur);

    /**
     * Partially updates a acheteur.
     *
     * @param acheteur the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Acheteur> partialUpdate(Acheteur acheteur);

    /**
     * Get all the acheteurs.
     *
     * @return the list of entities.
     */
    Flux<Acheteur> findAll();

    /**
     * Returns the number of acheteurs available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" acheteur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Acheteur> findOne(Long id);

    /**
     * Delete the "id" acheteur.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
