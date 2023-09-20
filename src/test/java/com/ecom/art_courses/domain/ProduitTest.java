package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class ProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Produit.class);
        // Creation of LigneCommande for tests
        LigneCommande ligneCommande1 = new LigneCommande();
        LigneCommande ligneCommande2 = new LigneCommande();
        Set<LigneCommande> commandesLigneSet = new HashSet<LigneCommande>();
        commandesLigneSet.add(ligneCommande1);

        // Creation of Commande for tests
        Commande commande1 = new Commande();
        Commande commande2 = new Commande();
        Set<Commande> commandesSet = new HashSet<Commande>();
        commandesSet.add(commande1);

        // Creation of Produit for tests
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
            .lienImg("/content/images/course images/broderie.jpg")
            .addLigneCommande(ligneCommande2)
            .addCommande(commande2)
            .promotion("10")
            .nomProf("Jean Paul")
            .version(2)
            .commandes(commandesSet)
            .ligneCommandes(commandesLigneSet);

        Produit produit2 = new Produit();
        produit2.setId(produit1.getId());
        produit2.setNomProduit(produit1.getNomProduit());
        produit2.setDesc(produit1.getDesc());
        produit2.setTarifUnit(produit1.getTarifUnit());
        produit2.setDuree(produit1.getDuree());
        produit2.setDate(produit1.getDate());
        produit2.setQuantiteTotale(produit1.getQuantiteTotale());
        produit2.setQuantiteDispo(produit1.getQuantiteDispo());
        //produit2.setSouscategorieId(produit1.getSouscategorieId());
        produit2.setSouscategorie(produit1.getSouscategorie());
        produit2.setLigneCommandes(produit1.getLigneCommandes());
        produit2.setCommandes(produit1.getCommandes());
        produit2.setNomProf("Jean Luc");
        produit2.setPromotion("10");
        produit2.setVersion(produit1.getVersion());

        assertThat(produit1).isEqualTo(produit2);
        produit2.setId(2L);
        produit2.updateAt(Instant.now());
        assertThat(produit1).isNotEqualTo(produit2);
        produit1.setId(null);
        assertThat(produit1).isNotEqualTo(produit2);
        assertThat(produit1.getLigneCommandes()).isEqualTo(produit2.getLigneCommandes());
        produit1.removeLigneCommande(ligneCommande2);
        produit1.removeLigneCommande(ligneCommande1);
        assertThat(produit1.getLigneCommandes().isEmpty());
        assertThat(produit1.getCommandes()).isEqualTo(produit2.getCommandes());
        produit1.removeCommande(commande1);
        produit1.removeCommande(commande2);
        assertThat(produit1.getCommandes().isEmpty());
        produit1.setNomProf("Jean Luc");
        assertThat(produit1.getNomProf()).isEqualTo(produit2.getNomProf());
        assertThat(produit1.getPromotion()).isEqualTo(produit2.getPromotion());
    }
}
