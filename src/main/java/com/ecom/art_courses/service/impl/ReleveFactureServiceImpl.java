package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.ReleveFactureRepository;
import com.ecom.art_courses.service.ReleveFactureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ReleveFacture}.
 */
@Service
@Transactional
public class ReleveFactureServiceImpl implements ReleveFactureService {

    private final Logger log = LoggerFactory.getLogger(ReleveFactureServiceImpl.class);

    private final ReleveFactureRepository releveFactureRepository;

    public ReleveFactureServiceImpl(ReleveFactureRepository releveFactureRepository) {
        this.releveFactureRepository = releveFactureRepository;
    }

    @Override
    public Mono<ReleveFacture> save(ReleveFacture releveFacture) {
        log.debug("Request to save ReleveFacture : {}", releveFacture);
        return releveFactureRepository.save(releveFacture);
    }

    @Override
    public Mono<ReleveFacture> update(ReleveFacture releveFacture) {
        log.debug("Request to update ReleveFacture : {}", releveFacture);
        return releveFactureRepository.save(releveFacture);
    }

    @Override
    public Mono<ReleveFacture> partialUpdate(ReleveFacture releveFacture) {
        log.debug("Request to partially update ReleveFacture : {}", releveFacture);

        return releveFactureRepository
            .findById(releveFacture.getId())
            .map(existingReleveFacture -> {
                if (releveFacture.getMontant() != null) {
                    existingReleveFacture.setMontant(releveFacture.getMontant());
                }
                if (releveFacture.getCreatedAt() != null) {
                    existingReleveFacture.setCreatedAt(releveFacture.getCreatedAt());
                }
                if (releveFacture.getUpdateAt() != null) {
                    existingReleveFacture.setUpdateAt(releveFacture.getUpdateAt());
                }

                return existingReleveFacture;
            })
            .flatMap(releveFactureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ReleveFacture> findAll() {
        log.debug("Request to get all ReleveFactures");
        return releveFactureRepository.findAll();
    }

    public Mono<Long> countAll() {
        return releveFactureRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ReleveFacture> findOne(Long id) {
        log.debug("Request to get ReleveFacture : {}", id);
        return releveFactureRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ReleveFacture : {}", id);
        return releveFactureRepository.deleteById(id);
    }
}
