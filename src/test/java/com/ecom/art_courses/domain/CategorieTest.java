package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategorieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categorie.class);
        Categorie categorie1 = new Categorie();
        categorie1.setId(1L);
        categorie1.createdAt(Instant.now()).typeCategorie("test category");
        Categorie categorie2 = new Categorie();
        categorie2.setId(categorie1.getId());
        assertThat(categorie1).isEqualTo(categorie2);
        categorie2.id(2L);
        assertThat(categorie1).isNotEqualTo(categorie2);
        categorie1.setId(null);
        categorie1.updateAt(Instant.now());
        assertThat(categorie1).isNotEqualTo(categorie2);

        TestUtil.equalsVerifier(SousCategorie.class);
        SousCategorie sousCategorie1 = new SousCategorie();
        sousCategorie1.id(1L);
        SousCategorie sousCategorie2 = new SousCategorie();
        sousCategorie1.id(2L);

        categorie1.addSousCategorie(sousCategorie1);
        assertThat(categorie1.getSousCategories().size()).isEqualTo(1);
        categorie1.addSousCategorie(sousCategorie2);
        assertThat(categorie1.getSousCategories().size()).isEqualTo(2);
        categorie1.removeSousCategorie(sousCategorie1);
        categorie1.removeSousCategorie(sousCategorie2);
        assertThat(categorie1.getSousCategories()).isEmpty();
    }
}
