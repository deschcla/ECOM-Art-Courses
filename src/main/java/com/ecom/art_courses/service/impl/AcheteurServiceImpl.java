//package com.ecom.art_courses.service.impl;
//
//import com.ecom.art_courses.domain.Acheteur;
//import com.ecom.art_courses.repository.AcheteurRepository;
//import com.ecom.art_courses.repository.UserRepository;
//import com.ecom.art_courses.service.AcheteurService;
//import java.util.List;
//import java.util.Optional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Service Implementation for managing {@link Acheteur}.
// */
//@Service
//@Transactional
//public class AcheteurServiceImpl implements AcheteurService {
//
//    private final Logger log = LoggerFactory.getLogger(AcheteurServiceImpl.class);
//
//    private final AcheteurRepository acheteurRepository;
//
//    private final UserRepository userRepository;
//
//    public AcheteurServiceImpl(AcheteurRepository acheteurRepository, UserRepository userRepository) {
//        this.acheteurRepository = acheteurRepository;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public Acheteur save(Acheteur acheteur) {
//        log.debug("Request to save Acheteur : {}", acheteur);
//         Long userId = acheteur.getInternalUser().getId();
////         userRepository.findById(userId).ifPresent(acheteur::user);
//        return acheteurRepository.save(acheteur);
//    }
//
//    @Override
//    public Acheteur update(Acheteur acheteur) {
//        log.debug("Request to update Acheteur : {}", acheteur);
//        Long userId = acheteur.getInternalUser().getId();
//        //        userRepository.findById(userId).ifPresent(acheteur::user);
//        return acheteurRepository.save(acheteur);
//    }
//
//    @Override
//    public Optional<Acheteur> partialUpdate(Acheteur acheteur) {
//        log.debug("Request to partially update Acheteur : {}", acheteur);
//
//        return acheteurRepository
//            .findById(acheteur.getId())
//            .map(existingAcheteur -> {
//                if (acheteur.getAdresse() != null) {
//                    existingAcheteur.setAdresse(acheteur.getAdresse());
//                }
//                if (acheteur.getDateNaiss() != null) {
//                    existingAcheteur.setDateNaiss(acheteur.getDateNaiss());
//                }
//                if (acheteur.getNumTel() != null) {
//                    existingAcheteur.setNumTel(acheteur.getNumTel());
//                }
//                if (acheteur.getCreatedAt() != null) {
//                    existingAcheteur.setCreatedAt(acheteur.getCreatedAt());
//                }
//                if (acheteur.getUpdateAt() != null) {
//                    existingAcheteur.setUpdateAt(acheteur.getUpdateAt());
//                }
//
//                return existingAcheteur;
//            })
//            .map(acheteurRepository::save);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Acheteur> findAll() {
//        log.debug("Request to get all Acheteurs");
//        return acheteurRepository.findAll();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<Acheteur> findOne(Long id) {
//        log.debug("Request to get Acheteur : {}", id);
//        return acheteurRepository.findById(id);
//    }
//
//    @Override
//    public void delete(Long id) {
//        log.debug("Request to delete Acheteur : {}", id);
//        acheteurRepository.deleteById(id);
//    }
//}
