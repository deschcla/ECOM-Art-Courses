package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.ReleveFacture;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ReleveFacture}.
 */
public interface ReleveFactureService {
    /**
     * Save a releveFacture.
     *
     * @param releveFacture the entity to save.
     * @return the persisted entity.
     */
    Mono<ReleveFacture> save(ReleveFacture releveFacture);

    /**
     * Updates a releveFacture.
     *
     * @param releveFacture the entity to update.
     * @return the persisted entity.
     */
    Mono<ReleveFacture> update(ReleveFacture releveFacture);

    /**
     * Partially updates a releveFacture.
     *
     * @param releveFacture the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ReleveFacture> partialUpdate(ReleveFacture releveFacture);

    /**
     * Get all the releveFactures.
     *
     * @return the list of entities.
     */
    Flux<ReleveFacture> findAll();

    /**
     * Returns the number of releveFactures available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" releveFacture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ReleveFacture> findOne(Long id);

    /**
     * Delete the "id" releveFacture.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
