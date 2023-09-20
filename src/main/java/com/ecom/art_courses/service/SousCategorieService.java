package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.SousCategorie;
import java.util.List;
import java.util.Optional;

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
    SousCategorie save(SousCategorie sousCategorie);

    /**
     * Updates a sousCategorie.
     *
     * @param sousCategorie the entity to update.
     * @return the persisted entity.
     */
    SousCategorie update(SousCategorie sousCategorie);

    /**
     * Partially updates a sousCategorie.
     *
     * @param sousCategorie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SousCategorie> partialUpdate(SousCategorie sousCategorie);

    /**
     * Get all the sousCategories.
     *
     * @return the list of entities.
     */
    List<SousCategorie> findAll();

    /**
     * Get the "id" sousCategorie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SousCategorie> findOne(Long id);

    /**
     * Delete the "id" sousCategorie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
