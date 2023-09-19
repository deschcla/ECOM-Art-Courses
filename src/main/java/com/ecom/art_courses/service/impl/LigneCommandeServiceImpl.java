package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.LigneCommande;
import com.ecom.art_courses.repository.LigneCommandeRepository;
import com.ecom.art_courses.service.LigneCommandeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public LigneCommande save(LigneCommande ligneCommande) {
        log.debug("Request to save LigneCommande : {}", ligneCommande);
        ligneCommande.getCommande().addLigneCommande(ligneCommande);
        return ligneCommandeRepository.save(ligneCommande);
    }

    @Override
    public LigneCommande update(LigneCommande ligneCommande) {
        log.debug("Request to update LigneCommande : {}", ligneCommande);
        return ligneCommandeRepository.save(ligneCommande);
    }

    @Override
    public Optional<LigneCommande> partialUpdate(LigneCommande ligneCommande) {
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
                if (ligneCommande.getValidated() != null) {
                    existingLigneCommande.setValidated(ligneCommande.getValidated());
                }
                if (ligneCommande.getNomParticipant() != null) {
                    existingLigneCommande.setNomParticipant(ligneCommande.getNomParticipant());
                }
                if (ligneCommande.getCreatedAt() != null) {
                    existingLigneCommande.setCreatedAt(ligneCommande.getCreatedAt());
                }
                if (ligneCommande.getUpdateAt() != null) {
                    existingLigneCommande.setUpdateAt(ligneCommande.getUpdateAt());
                }
                if (ligneCommande.getPanier() != null) {
                    existingLigneCommande.setPanier(ligneCommande.getPanier());
                }

                return existingLigneCommande;
            })
            .map(ligneCommandeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneCommande> findAll() {
        log.debug("Request to get all LigneCommandes");
        return ligneCommandeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LigneCommande> findOne(Long id) {
        log.debug("Request to get LigneCommande : {}", id);
        return ligneCommandeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LigneCommande : {}", id);
        ligneCommandeRepository.deleteById(id);
    }
}
