package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.ReleveFacture;
import java.util.List;
import java.util.Optional;

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
    ReleveFacture save(ReleveFacture releveFacture);

    /**
     * Updates a releveFacture.
     *
     * @param releveFacture the entity to update.
     * @return the persisted entity.
     */
    ReleveFacture update(ReleveFacture releveFacture);

    /**
     * Partially updates a releveFacture.
     *
     * @param releveFacture the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReleveFacture> partialUpdate(ReleveFacture releveFacture);

    /**
     * Get all the releveFactures.
     *
     * @return the list of entities.
     */
    List<ReleveFacture> findAll();

    /**
     * Get the "id" releveFacture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReleveFacture> findOne(Long id);

    /**
     * Delete the "id" releveFacture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
