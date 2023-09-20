package com.ecom.art_courses.domain;

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

        // Creation of SousCategorie for tests
        SousCategorie sousCategorie1 = new SousCategorie();
        SousCategorie sousCategorie2 = new SousCategorie();
        Set<SousCategorie> sousCategorieSet = new HashSet<SousCategorie>();
        sousCategorieSet.add(sousCategorie1);

        Categorie categorie1 = new Categorie();
        categorie1.id(1L).createdAt(Instant.now()).updateAt(Instant.now()).sousCategories(sousCategorieSet).typeCategorie("Peinture");

        Categorie categorie2 = new Categorie();
        categorie2.setId(categorie1.getId());
        assertThat(categorie1).isEqualTo(categorie2);
        categorie2.setId(2L);
        categorie2.setTypeCategorie("Musique");
        assertThat(categorie1).isNotEqualTo(categorie2);
        categorie1.setId(null);
        assertThat(categorie1).isNotEqualTo(categorie2);
        categorie2.setCreatedAt(categorie1.getCreatedAt());
        categorie2.setUpdateAt(categorie1.getUpdateAt());
        categorie2.setSousCategories(categorie1.getSousCategories());
        assertThat(categorie1.getSousCategories()).isEqualTo(categorie2.getSousCategories());
        categorie2.addSousCategorie(sousCategorie2);
        categorie2.removeSousCategorie(sousCategorie2);
        assertThat(categorie1.getSousCategories()).isEqualTo(categorie2.getSousCategories());
    }
}
