package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.LigneCommande;
import java.util.List;
import java.util.Optional;

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
    LigneCommande save(LigneCommande ligneCommande);

    /**
     * Updates a ligneCommande.
     *
     * @param ligneCommande the entity to update.
     * @return the persisted entity.
     */
    LigneCommande update(LigneCommande ligneCommande);

    /**
     * Partially updates a ligneCommande.
     *
     * @param ligneCommande the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LigneCommande> partialUpdate(LigneCommande ligneCommande);

    /**
     * Get all the ligneCommandes.
     *
     * @return the list of entities.
     */
    List<LigneCommande> findAll();

    /**
     * Get the "id" ligneCommande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LigneCommande> findOne(Long id);

    /**
     * Delete the "id" ligneCommande.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
