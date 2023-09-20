package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.repository.CommandeRepository;
import com.ecom.art_courses.service.CommandeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commande}.
 */
@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final Logger log = LoggerFactory.getLogger(CommandeServiceImpl.class);

    private final CommandeRepository commandeRepository;

    public CommandeServiceImpl(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Commande save(Commande commande) {
        log.debug("Request to save Commande : {}", commande);
        return commandeRepository.save(commande);
    }

    @Override
    public Commande update(Commande commande) {
        log.debug("Request to update Commande : {}", commande);
        return commandeRepository.save(commande);
    }

    @Override
    public Optional<Commande> partialUpdate(Commande commande) {
        log.debug("Request to partially update Commande : {}", commande);

        return commandeRepository
            .findById(commande.getId())
            .map(existingCommande -> {
                if (commande.getMontant() != null) {
                    existingCommande.setMontant(commande.getMontant());
                }
                if (commande.getValidated() != null) {
                    existingCommande.setValidated(commande.getValidated());
                }
                if (commande.getCreatedAt() != null) {
                    existingCommande.setCreatedAt(commande.getCreatedAt());
                }
                if (commande.getUpdateAt() != null) {
                    existingCommande.setUpdateAt(commande.getUpdateAt());
                }

                return existingCommande;
            })
            .map(commandeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commande> findAll() {
        log.debug("Request to get all Commandes");
        return commandeRepository.findAll();
    }

    public Page<Commande> findAllWithEagerRelationships(Pageable pageable) {
        return commandeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Commande> findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        return commandeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        commandeRepository.deleteById(id);
    }
}
