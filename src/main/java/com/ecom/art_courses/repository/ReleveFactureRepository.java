package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.ReleveFacture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReleveFacture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleveFactureRepository extends JpaRepository<ReleveFacture, Long> {}
