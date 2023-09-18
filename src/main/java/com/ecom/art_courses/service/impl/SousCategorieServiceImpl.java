package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.SousCategorie;
import com.ecom.art_courses.repository.SousCategorieRepository;
import com.ecom.art_courses.service.SousCategorieService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousCategorie}.
 */
@Service
@Transactional
public class SousCategorieServiceImpl implements SousCategorieService {

    private final Logger log = LoggerFactory.getLogger(SousCategorieServiceImpl.class);

    private final SousCategorieRepository sousCategorieRepository;

    public SousCategorieServiceImpl(SousCategorieRepository sousCategorieRepository) {
        this.sousCategorieRepository = sousCategorieRepository;
    }

    @Override
    public SousCategorie save(SousCategorie sousCategorie) {
        log.debug("Request to save SousCategorie : {}", sousCategorie);
        return sousCategorieRepository.save(sousCategorie);
    }

    @Override
    public SousCategorie update(SousCategorie sousCategorie) {
        log.debug("Request to update SousCategorie : {}", sousCategorie);
        return sousCategorieRepository.save(sousCategorie);
    }

    @Override
    public Optional<SousCategorie> partialUpdate(SousCategorie sousCategorie) {
        log.debug("Request to partially update SousCategorie : {}", sousCategorie);

        return sousCategorieRepository
            .findById(sousCategorie.getId())
            .map(existingSousCategorie -> {
                if (sousCategorie.getTypeSousCategorie() != null) {
                    existingSousCategorie.setTypeSousCategorie(sousCategorie.getTypeSousCategorie());
                }
                if (sousCategorie.getCreatedAt() != null) {
                    existingSousCategorie.setCreatedAt(sousCategorie.getCreatedAt());
                }
                if (sousCategorie.getUpdateAt() != null) {
                    existingSousCategorie.setUpdateAt(sousCategorie.getUpdateAt());
                }

                return existingSousCategorie;
            })
            .map(sousCategorieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SousCategorie> findAll() {
        log.debug("Request to get all SousCategories");
        return sousCategorieRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SousCategorie> findOne(Long id) {
        log.debug("Request to get SousCategorie : {}", id);
        return sousCategorieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SousCategorie : {}", id);
        sousCategorieRepository.deleteById(id);
    }
}
