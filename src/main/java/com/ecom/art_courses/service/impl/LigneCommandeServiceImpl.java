package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.LigneCommande;
import com.ecom.art_courses.repository.LigneCommandeRepository;
import com.ecom.art_courses.service.LigneCommandeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link LigneCommande}.
 */
@Service
@Transactional
public class LigneCommandeServiceImpl implements LigneCommandeService {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeServiceImpl.class);

    private final LigneCommandeRepository ligneCommandeRepository;

    public LigneCommandeServiceImpl(LigneCommandeRepository ligneCommandeRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    @Override
    public Mono<LigneCommande> save(LigneCommande ligneCommande) {
        log.debug("Request to save LigneCommande : {}", ligneCommande);
        return ligneCommandeRepository.save(ligneCommande);
    }

    @Override
    public Mono<LigneCommande> update(LigneCommande ligneCommande) {
        log.debug("Request to update LigneCommande : {}", ligneCommande);
        return ligneCommandeRepository.save(ligneCommande);
    }

    @Override
    public Mono<LigneCommande> partialUpdate(LigneCommande ligneCommande) {
        log.debug("Request to partially update LigneCommande : {}", ligneCommande);

        return ligneCommandeRepository
            .findById(ligneCommande.getId())
            .map(existingLigneCommande -> {
                if (ligneCommande.getQuantite() != null) {
                    existingLigneCommande.setQuantite(ligneCommande.getQuantite());
                }
                if (ligneCommande.getMontant() != null) {
                    existingLigneCommande.setMontant(ligneCommande.getMontant());
                }
                if (ligneCommande.getValided() != null) {
                    existingLigneCommande.setValided(ligneCommande.getValided());
                }
                if (ligneCommande.getCreatedAt() != null) {
                    existingLigneCommande.setCreatedAt(ligneCommande.getCreatedAt());
                }
                if (ligneCommande.getUpdateAt() != null) {
                    existingLigneCommande.setUpdateAt(ligneCommande.getUpdateAt());
                }

                return existingLigneCommande;
            })
            .flatMap(ligneCommandeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<LigneCommande> findAll() {
        log.debug("Request to get all LigneCommandes");
        return ligneCommandeRepository.findAll();
    }

    public Mono<Long> countAll() {
        return ligneCommandeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<LigneCommande> findOne(Long id) {
        log.debug("Request to get LigneCommande : {}", id);
        return ligneCommandeRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete LigneCommande : {}", id);
        return ligneCommandeRepository.deleteById(id);
    }
}
