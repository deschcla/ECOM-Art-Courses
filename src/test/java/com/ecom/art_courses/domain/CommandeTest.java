package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CommandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commande.class);

        // CarteBancaire for tests
        CarteBancaire carteBancaire1 = new CarteBancaire();
        CarteBancaire carteBancaire2 = new CarteBancaire();
        Set<CarteBancaire> carteBancairesSet = new HashSet<CarteBancaire>();
        carteBancairesSet.add(carteBancaire1);

        // Produit for tests
        Produit produit1 = new Produit();
        Produit produit2 = new Produit();
        Set<Produit> produitSet = new HashSet<Produit>();
        produitSet.add(produit1);

        // Creation of LigneCommande for tests
        LigneCommande ligneCommande1 = new LigneCommande();
        LigneCommande ligneCommande2 = new LigneCommande();
        Set<LigneCommande> commandesLigneSet = new HashSet<LigneCommande>();
        commandesLigneSet.add(ligneCommande1);

        // Commande for tests
        Commande commande1 = new Commande();
        commande1
            .id(1L)
            .montant((float) 25.50)
            .validated(0)
            .createdAt(Instant.now())
            .updateAt(Instant.now())
            .carteBancaires(carteBancairesSet)
            .addCarteBancaire(carteBancaire2)
            .produits(produitSet)
            .releveFacture(new ReleveFacture())
            .ligneCommandes(commandesLigneSet)
            .addLigneCommande(ligneCommande2);

        Commande commande2 = new Commande();
        commande2.setId(commande1.getId());
        assertThat(commande1).isEqualTo(commande2);
        commande2.setId(2L);
        assertThat(commande1).isNotEqualTo(commande2);
        commande1.setId(null);
        assertThat(commande1).isNotEqualTo(commande2);
        commande2.setMontant((float) 25.50);
        assertThat(commande1.getMontant()).isEqualTo(commande2.getMontant());
        commande2.setValidated(0);
        assertThat(commande1.getValidated()).isEqualTo(commande2.getValidated());
        commande2.setCreatedAt(commande1.getCreatedAt());
        commande2.setUpdateAt(commande1.getUpdateAt());
        commande2.setCarteBancaires(commande1.getCarteBancaires());
        assertThat(commande1.getCarteBancaires()).isEqualTo(commande2.getCarteBancaires());
        commande1.removeCarteBancaire(carteBancaire1);
        commande2.removeCarteBancaire(carteBancaire1);
        assertThat(commande1.getCarteBancaires()).isEqualTo(commande2.getCarteBancaires());
        commande2.setProduits(commande1.getProduits());
        commande2.addProduit(produit2);
        commande2.removeProduit(produit2);
        assertThat(commande1.getProduits()).isEqualTo(commande2.getProduits());
        assertThat(commande1.getReleveFacture()).isNotEqualTo(commande2.getReleveFacture());
        commande2.setLigneCommandes(commande1.getLigneCommandes());
        assertThat(commande1.getLigneCommandes()).isEqualTo(commande2.getLigneCommandes());
        commande1.removeLigneCommande(ligneCommande2);
        commande1.removeLigneCommande(ligneCommande1);
        assertThat(commande1.getLigneCommandes().isEmpty());
    }
    //    @Test
    //    void commander() throws Exception {
    //        TestUtil.equalsVerifier(Commande.class);
    //        TestUtil.equalsVerifier(LigneCommande.class);
    //        TestUtil.equalsVerifier(Produit.class);
    //        TestUtil.equalsVerifier(Categorie.class);
    //        TestUtil.equalsVerifier(SousCategorie.class);
    //        TestUtil.equalsVerifier(CarteBancaire.class);
    //        Commande commande3 = new Commande();
    //    }
}
