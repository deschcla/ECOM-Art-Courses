package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.ReleveFactureRepository;
import com.ecom.art_courses.service.ReleveFactureService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ReleveFacture save(ReleveFacture releveFacture) {
        log.debug("Request to save ReleveFacture : {}", releveFacture);
        return releveFactureRepository.save(releveFacture);
    }

    @Override
    public ReleveFacture update(ReleveFacture releveFacture) {
        log.debug("Request to update ReleveFacture : {}", releveFacture);
        return releveFactureRepository.save(releveFacture);
    }

    @Override
    public Optional<ReleveFacture> partialUpdate(ReleveFacture releveFacture) {
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
            .map(releveFactureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleveFacture> findAll() {
        log.debug("Request to get all ReleveFactures");
        return releveFactureRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReleveFacture> findOne(Long id) {
        log.debug("Request to get ReleveFacture : {}", id);
        return releveFactureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReleveFacture : {}", id);
        releveFactureRepository.deleteById(id);
    }
}
