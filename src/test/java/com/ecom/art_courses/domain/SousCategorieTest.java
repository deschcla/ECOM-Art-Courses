package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousCategorieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousCategorie.class);
        SousCategorie sousCategorie1 = new SousCategorie();
        sousCategorie1.setId(1L);
        SousCategorie sousCategorie2 = new SousCategorie();
        sousCategorie2.setId(sousCategorie1.getId());
        assertThat(sousCategorie1).isEqualTo(sousCategorie2);
        sousCategorie2.setId(2L);
        assertThat(sousCategorie1).isNotEqualTo(sousCategorie2);
        sousCategorie1.setId(null);
        assertThat(sousCategorie1).isNotEqualTo(sousCategorie2);
    }
}
