package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.repository.CommandeRepository;
import com.ecom.art_courses.service.CommandeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Commande> save(Commande commande) {
        log.debug("Request to save Commande : {}", commande);
        return commandeRepository.save(commande);
    }

    @Override
    public Mono<Commande> update(Commande commande) {
        log.debug("Request to update Commande : {}", commande);
        return commandeRepository.save(commande);
    }

    @Override
    public Mono<Commande> partialUpdate(Commande commande) {
        log.debug("Request to partially update Commande : {}", commande);

        return commandeRepository
            .findById(commande.getId())
            .map(existingCommande -> {
                if (commande.getMontant() != null) {
                    existingCommande.setMontant(commande.getMontant());
                }
                if (commande.getValided() != null) {
                    existingCommande.setValided(commande.getValided());
                }
                if (commande.getCreatedAt() != null) {
                    existingCommande.setCreatedAt(commande.getCreatedAt());
                }
                if (commande.getUpdateAt() != null) {
                    existingCommande.setUpdateAt(commande.getUpdateAt());
                }

                return existingCommande;
            })
            .flatMap(commandeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Commande> findAll() {
        log.debug("Request to get all Commandes");
        return commandeRepository.findAll();
    }

    public Flux<Commande> findAllWithEagerRelationships(Pageable pageable) {
        return commandeRepository.findAllWithEagerRelationships(pageable);
    }

    public Mono<Long> countAll() {
        return commandeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Commande> findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        return commandeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        return commandeRepository.deleteById(id);
    }
}
