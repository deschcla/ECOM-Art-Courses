package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class SousCategorieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousCategorie.class);
        TestUtil.equalsVerifier(Categorie.class);
        TestUtil.equalsVerifier(Produit.class);

        // Produit for tests
        Produit produit1 = new Produit().id(1L);
        Produit produit2 = new Produit();
        Set<Produit> produitSet = new HashSet<Produit>();
        produitSet.add(produit1);

        Categorie categorie1 = new Categorie().id(1L);
        categorie1.setTypeCategorie("Musique");
        Set<Produit> setProduits = new HashSet<Produit>();
        setProduits.add(produit1);
        SousCategorie sousCategorie1 = new SousCategorie();
        sousCategorie1
            .id(1L)
            .createdAt(Instant.now())
            .updateAt(Instant.now())
            .typeSousCategorie("musique")
            .categorie(categorie1)
            .produits(setProduits);

        SousCategorie sousCategorie2 = new SousCategorie();
        sousCategorie2.setId(sousCategorie1.getId());
        assertThat(sousCategorie1).isEqualTo(sousCategorie2);
        sousCategorie2.setId(2L);
        assertThat(sousCategorie1).isNotEqualTo(sousCategorie2);
        sousCategorie1.setId(null);
        assertThat(sousCategorie1).isNotEqualTo(sousCategorie2);
        sousCategorie2.setUpdateAt(sousCategorie1.getUpdateAt());
        sousCategorie2.setProduits(sousCategorie1.getProduits());
        sousCategorie2.addProduit(produit2);
        sousCategorie2.removeProduit(produit2);
        assertThat(sousCategorie1.getProduits()).isEqualTo(sousCategorie2.getProduits());
        assertThat(sousCategorie2.getCategorie()).isNotEqualTo(sousCategorie1);
        assertThat(sousCategorie1.getCategorieName()).isEqualTo("Musique");
    }
}
