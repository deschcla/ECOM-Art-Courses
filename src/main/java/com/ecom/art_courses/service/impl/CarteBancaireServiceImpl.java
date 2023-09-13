package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.CarteBancaire;
import com.ecom.art_courses.repository.CarteBancaireRepository;
import com.ecom.art_courses.service.CarteBancaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CarteBancaire}.
 */
@Service
@Transactional
public class CarteBancaireServiceImpl implements CarteBancaireService {

    private final Logger log = LoggerFactory.getLogger(CarteBancaireServiceImpl.class);

    private final CarteBancaireRepository carteBancaireRepository;

    public CarteBancaireServiceImpl(CarteBancaireRepository carteBancaireRepository) {
        this.carteBancaireRepository = carteBancaireRepository;
    }

    @Override
    public Mono<CarteBancaire> save(CarteBancaire carteBancaire) {
        log.debug("Request to save CarteBancaire : {}", carteBancaire);
        return carteBancaireRepository.save(carteBancaire);
    }

    @Override
    public Mono<CarteBancaire> update(CarteBancaire carteBancaire) {
        log.debug("Request to update CarteBancaire : {}", carteBancaire);
        return carteBancaireRepository.save(carteBancaire);
    }

    @Override
    public Mono<CarteBancaire> partialUpdate(CarteBancaire carteBancaire) {
        log.debug("Request to partially update CarteBancaire : {}", carteBancaire);

        return carteBancaireRepository
            .findById(carteBancaire.getId())
            .map(existingCarteBancaire -> {
                if (carteBancaire.getRefCarte() != null) {
                    existingCarteBancaire.setRefCarte(carteBancaire.getRefCarte());
                }
                if (carteBancaire.getCreatedAt() != null) {
                    existingCarteBancaire.setCreatedAt(carteBancaire.getCreatedAt());
                }
                if (carteBancaire.getUpdateAt() != null) {
                    existingCarteBancaire.setUpdateAt(carteBancaire.getUpdateAt());
                }

                return existingCarteBancaire;
            })
            .flatMap(carteBancaireRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CarteBancaire> findAll() {
        log.debug("Request to get all CarteBancaires");
        return carteBancaireRepository.findAll();
    }

    public Mono<Long> countAll() {
        return carteBancaireRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CarteBancaire> findOne(Long id) {
        log.debug("Request to get CarteBancaire : {}", id);
        return carteBancaireRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CarteBancaire : {}", id);
        return carteBancaireRepository.deleteById(id);
    }
}
