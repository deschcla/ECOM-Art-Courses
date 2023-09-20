package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.SousCategorie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SousCategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousCategorieRepository extends JpaRepository<SousCategorie, Long> {}
