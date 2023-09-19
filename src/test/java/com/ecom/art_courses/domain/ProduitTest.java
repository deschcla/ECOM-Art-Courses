package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class ProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Produit.class);
        Produit produit1 = new Produit();
        produit1.id(1L).createdAt(Instant.now());
        produit1
            .nomProduit("test")
            .desc("produit de test")
            .tarifUnit(10F)
            .date(ZonedDateTime.now())
            .duree("2")
            .quantiteTotale(20)
            .quantiteDispo(10)
            .nomProf("test prof")
            .promotion("10")
            .version(1);

        Produit produit2 = new Produit();
        produit2.setId(produit1.getId());
        assertThat(produit1).isEqualTo(produit2);
        produit2.setId(2L);
        assertThat(produit1).isNotEqualTo(produit2);
        produit1.id(null).version(2).updateAt(Instant.now());
        assertThat(produit1).isNotEqualTo(produit2);
    }
}
