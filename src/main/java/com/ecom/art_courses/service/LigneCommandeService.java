package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.LigneCommande;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link LigneCommande}.
 */
public interface LigneCommandeService {
    /**
     * Save a ligneCommande.
     *
     * @param ligneCommande the entity to save.
     * @return the persisted entity.
     */
    Mono<LigneCommande> save(LigneCommande ligneCommande);

    /**
     * Updates a ligneCommande.
     *
     * @param ligneCommande the entity to update.
     * @return the persisted entity.
     */
    Mono<LigneCommande> update(LigneCommande ligneCommande);

    /**
     * Partially updates a ligneCommande.
     *
     * @param ligneCommande the entity to update partially.
     * @return the persisted entity.
     */
    Mono<LigneCommande> partialUpdate(LigneCommande ligneCommande);

    /**
     * Get all the ligneCommandes.
     *
     * @return the list of entities.
     */
    Flux<LigneCommande> findAll();

    /**
     * Returns the number of ligneCommandes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" ligneCommande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<LigneCommande> findOne(Long id);

    /**
     * Delete the "id" ligneCommande.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
