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
        produit1
            .id(1L)
            .nomProduit("Cours de Piano")
            .desc(
                "Découvrez le monde de la musique à travers les touches du piano! Notre cours de piano offre un voyage passionnant pour les débutants et avancés. Vous développerez des compétences techniques, une expression artistique et un lien profond avec la musique. Rejoignez-nous et embarquez pour une aventure musicale qui enrichira votre vie et vous procurera une joie durable."
            )
            .tarifUnit((float) 19.9)
            .date(ZonedDateTime.now())
            .duree("2h")
            .quantiteTotale(20)
            .quantiteDispo(15)
            .souscategorie(new SousCategorie())
            .createdAt(Instant.now())
            .lienImg("/content/images/course images/broderie.jpg");

        Produit produit2 = new Produit();
        produit2.setId(produit1.getId());
        produit2.setNomProduit(produit1.getNomProduit());
        produit2.setDesc(produit1.getDesc());
        produit2.setTarifUnit(produit1.getTarifUnit());
        produit2.setDuree(produit1.getDuree());
        produit2.setDate(produit1.getDate());
        produit2.setQuantiteTotale(produit1.getQuantiteTotale());
        produit2.setQuantiteDispo(produit1.getQuantiteDispo());
        //        produit2.setSouscategorieId(produit1.getSouscategorieId());

        assertThat(produit1).isEqualTo(produit2);
        produit2.setId(2L);
        produit2.updateAt(Instant.now());
        assertThat(produit1).isNotEqualTo(produit2);
        produit1.setId(null);
        assertThat(produit1).isNotEqualTo(produit2);
    }
}
