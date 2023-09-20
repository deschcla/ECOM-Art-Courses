package com.ecom.art_courses.service;

import com.ecom.art_courses.domain.Produit;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Produit}.
 */
public interface ProduitService {
    /**
     * Save a produit.
     *
     * @param produit the entity to save.
     * @return the persisted entity.
     */
    Produit save(Produit produit);

    /**
     * Updates a produit.
     *
     * @param produit the entity to update.
     * @return the persisted entity.
     */
    Produit update(Produit produit);

    /**
     * Partially updates a produit.
     *
     * @param produit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Produit> partialUpdate(Produit produit);

    /**
     * Get all the produits.
     *
     * @return the list of entities.
     */
    List<Produit> findAll();

    /**
     * Get the "id" produit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Produit> findOne(Long id);

    /**
     * Delete the "id" produit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
