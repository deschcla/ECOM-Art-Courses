package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.CarteBancaire;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link CarteBancaire}.
 */
public interface CarteBancaireService {
    /**
     * Save a carteBancaire.
     *
     * @param carteBancaire the entity to save.
     * @return the persisted entity.
     */
    Mono<CarteBancaire> save(CarteBancaire carteBancaire);

    /**
     * Updates a carteBancaire.
     *
     * @param carteBancaire the entity to update.
     * @return the persisted entity.
     */
    Mono<CarteBancaire> update(CarteBancaire carteBancaire);

    /**
     * Partially updates a carteBancaire.
     *
     * @param carteBancaire the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CarteBancaire> partialUpdate(CarteBancaire carteBancaire);

    /**
     * Get all the carteBancaires.
     *
     * @return the list of entities.
     */
    Flux<CarteBancaire> findAll();

    /**
     * Returns the number of carteBancaires available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" carteBancaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CarteBancaire> findOne(Long id);

    /**
     * Delete the "id" carteBancaire.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
