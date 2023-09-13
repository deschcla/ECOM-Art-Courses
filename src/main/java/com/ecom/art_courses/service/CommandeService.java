package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.Commande;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link Commande}.
 */
public interface CommandeService {
    /**
     * Save a commande.
     *
     * @param commande the entity to save.
     * @return the persisted entity.
     */
    Mono<Commande> save(Commande commande);

    /**
     * Updates a commande.
     *
     * @param commande the entity to update.
     * @return the persisted entity.
     */
    Mono<Commande> update(Commande commande);

    /**
     * Partially updates a commande.
     *
     * @param commande the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Commande> partialUpdate(Commande commande);

    /**
     * Get all the commandes.
     *
     * @return the list of entities.
     */
    Flux<Commande> findAll();

    /**
     * Get all the commandes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Commande> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of commandes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" commande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Commande> findOne(Long id);

    /**
     * Delete the "id" commande.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
