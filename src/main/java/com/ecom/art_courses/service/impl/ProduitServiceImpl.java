package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.repository.ProduitRepository;
import com.ecom.art_courses.service.ProduitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Produit> save(Produit produit) {
        log.debug("Request to save Produit : {}", produit);
        return produitRepository.save(produit);
    }

    @Override
    public Mono<Produit> update(Produit produit) {
        log.debug("Request to update Produit : {}", produit);
        return produitRepository.save(produit);
    }

    @Override
    public Mono<Produit> partialUpdate(Produit produit) {
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
            .flatMap(produitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Produit> findAll() {
        log.debug("Request to get all Produits");
        return produitRepository.findAll();
    }

    public Mono<Long> countAll() {
        return produitRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Produit> findOne(Long id) {
        log.debug("Request to get Produit : {}", id);
        return produitRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Produit : {}", id);
        return produitRepository.deleteById(id);
    }
}
