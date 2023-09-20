package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.CarteBancaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarteBancaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarteBancaireRepository extends JpaRepository<CarteBancaire, Long> {}
