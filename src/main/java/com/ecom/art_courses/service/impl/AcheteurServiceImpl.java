package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.Acheteur;
import com.ecom.art_courses.repository.AcheteurRepository;
import com.ecom.art_courses.service.AcheteurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Acheteur}.
 */
@Service
@Transactional
public class AcheteurServiceImpl implements AcheteurService {

    private final Logger log = LoggerFactory.getLogger(AcheteurServiceImpl.class);

    private final AcheteurRepository acheteurRepository;

    public AcheteurServiceImpl(AcheteurRepository acheteurRepository) {
        this.acheteurRepository = acheteurRepository;
    }

    @Override
    public Mono<Acheteur> save(Acheteur acheteur) {
        log.debug("Request to save Acheteur : {}", acheteur);
        return acheteurRepository.save(acheteur);
    }

    @Override
    public Mono<Acheteur> update(Acheteur acheteur) {
        log.debug("Request to update Acheteur : {}", acheteur);
        return acheteurRepository.save(acheteur);
    }

    @Override
    public Mono<Acheteur> partialUpdate(Acheteur acheteur) {
        log.debug("Request to partially update Acheteur : {}", acheteur);

        return acheteurRepository
            .findById(acheteur.getId())
            .map(existingAcheteur -> {
                if (acheteur.getAdresse() != null) {
                    existingAcheteur.setAdresse(acheteur.getAdresse());
                }
                if (acheteur.getDateNaiss() != null) {
                    existingAcheteur.setDateNaiss(acheteur.getDateNaiss());
                }
                if (acheteur.getNumTel() != null) {
                    existingAcheteur.setNumTel(acheteur.getNumTel());
                }
                if (acheteur.getCreatedAt() != null) {
                    existingAcheteur.setCreatedAt(acheteur.getCreatedAt());
                }
                if (acheteur.getUpdateAt() != null) {
                    existingAcheteur.setUpdateAt(acheteur.getUpdateAt());
                }

                return existingAcheteur;
            })
            .flatMap(acheteurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Acheteur> findAll() {
        log.debug("Request to get all Acheteurs");
        return acheteurRepository.findAll();
    }

    public Mono<Long> countAll() {
        return acheteurRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Acheteur> findOne(Long id) {
        log.debug("Request to get Acheteur : {}", id);
        return acheteurRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Acheteur : {}", id);
        return acheteurRepository.deleteById(id);
    }
}
