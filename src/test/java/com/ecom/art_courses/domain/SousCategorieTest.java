package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class SousCategorieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousCategorie.class);
        SousCategorie sousCategorie1 = new SousCategorie();
        sousCategorie1.id(1L).createdAt(Instant.now());
        sousCategorie1.typeSousCategorie("sous categorie de test");
        SousCategorie sousCategorie2 = new SousCategorie();
        sousCategorie2.setId(sousCategorie1.getId());
        assertThat(sousCategorie1).isEqualTo(sousCategorie2);
        sousCategorie2.setId(2L);
        assertThat(sousCategorie1).isNotEqualTo(sousCategorie2);
        sousCategorie1.id(null).updateAt(Instant.now());
        assertThat(sousCategorie1).isNotEqualTo(sousCategorie2);

        TestUtil.equalsVerifier(Produit.class);
        Produit produit = new Produit();
        produit.id(2L);
        sousCategorie2.addProduit(produit);
        assertThat(sousCategorie2.getProduits()).isNotNull();
        sousCategorie2.removeProduit(produit);
        assertThat(sousCategorie2.getProduits()).isEmpty();
    }
}
