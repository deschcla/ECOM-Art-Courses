package com.ecom.art_courses.service.impl;

import com.ecom.art_courses.domain.CarteBancaire;
import com.ecom.art_courses.repository.CarteBancaireRepository;
import com.ecom.art_courses.service.CarteBancaireService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CarteBancaire save(CarteBancaire carteBancaire) {
        log.debug("Request to save CarteBancaire : {}", carteBancaire);
        return carteBancaireRepository.save(carteBancaire);
    }

    @Override
    public CarteBancaire update(CarteBancaire carteBancaire) {
        log.debug("Request to update CarteBancaire : {}", carteBancaire);
        return carteBancaireRepository.save(carteBancaire);
    }

    @Override
    public Optional<CarteBancaire> partialUpdate(CarteBancaire carteBancaire) {
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
            .map(carteBancaireRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarteBancaire> findAll() {
        log.debug("Request to get all CarteBancaires");
        return carteBancaireRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarteBancaire> findOne(Long id) {
        log.debug("Request to get CarteBancaire : {}", id);
        return carteBancaireRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarteBancaire : {}", id);
        carteBancaireRepository.deleteById(id);
    }
}
