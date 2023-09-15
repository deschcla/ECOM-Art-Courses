package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.repository.ProduitRepository;
import com.ecom.art_courses.service.ProduitService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Produit}.
 */
@Service
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final Logger log = LoggerFactory.getLogger(ProduitServiceImpl.class);

    private final ProduitRepository produitRepository;

    public ProduitServiceImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public Produit save(Produit produit) {
        log.debug("Request to save Produit : {}", produit);
        return produitRepository.save(produit);
    }

    @Override
    public Produit update(Produit produit) {
        log.debug("Request to update Produit : {}", produit);
        return produitRepository.save(produit);
    }

    @Override
    public Optional<Produit> partialUpdate(Produit produit) {
        log.debug("Request to partially update Produit : {}", produit);

        return produitRepository
            .findById(produit.getId())
            .map(existingProduit -> {
                if (produit.getNomProduit() != null) {
                    existingProduit.setNomProduit(produit.getNomProduit());
                }
                if (produit.getDesc() != null) {
                    existingProduit.setDesc(produit.getDesc());
                }
                if (produit.getTarifUnit() != null) {
                    existingProduit.setTarifUnit(produit.getTarifUnit());
                }
                if (produit.getDate() != null) {
                    existingProduit.setDate(produit.getDate());
                }
                if (produit.getDuree() != null) {
                    existingProduit.setDuree(produit.getDuree());
                }
                if (produit.getLienImg() != null) {
                    existingProduit.setLienImg(produit.getLienImg());
                }
                if (produit.getQuantiteTotale() != null) {
                    existingProduit.setQuantiteTotale(produit.getQuantiteTotale());
                }
                if (produit.getQuantiteDispo() != null) {
                    existingProduit.setQuantiteDispo(produit.getQuantiteDispo());
                }
                if (produit.getCreatedAt() != null) {
                    existingProduit.setCreatedAt(produit.getCreatedAt());
                }
                if (produit.getUpdateAt() != null) {
                    existingProduit.setUpdateAt(produit.getUpdateAt());
                }

                return existingProduit;
            })
            .map(produitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> findAll() {
        log.debug("Request to get all Produits");
        return produitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produit> findOne(Long id) {
        log.debug("Request to get Produit : {}", id);
        return produitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Produit : {}", id);
        produitRepository.deleteById(id);
    }
}
