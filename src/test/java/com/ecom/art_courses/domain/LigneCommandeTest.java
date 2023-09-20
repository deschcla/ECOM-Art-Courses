package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LigneCommandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCommande.class);

        // Produit for tests
        Produit produit1 = new Produit();
        Produit produit2 = new Produit();
        Set<Produit> produitSet = new HashSet<Produit>();
        produitSet.add(produit1);

        Commande commande1 = new Commande();
        Commande commande2 = new Commande();
        Set<Commande> commandesSet = new HashSet<Commande>();
        commandesSet.add(commande1);

        LigneCommande ligneCommande1 = new LigneCommande();
        ligneCommande1
            .id(1L)
            .quantite(2)
            .montant((float) 20.00)
            .validated(0)
            .nomParticipant("Jean")
            .createdAt(Instant.now())
            .updateAt(Instant.now())
            .produit(produit1)
            .commande(commande1);

        LigneCommande ligneCommande2 = new LigneCommande();
        ligneCommande2.setId(ligneCommande1.getId());
        ligneCommande2.setQuantite(3);
        ligneCommande2.setMontant((float) 20.00);
        ligneCommande2.setValidated(1);
        ligneCommande2.setNomParticipant("Jean");
        ligneCommande2.setProduit(produit2);
        ligneCommande2.setCommande(commande2);
        ligneCommande2.setCreatedAt(ligneCommande1.getCreatedAt());
        ligneCommande2.setUpdateAt(ligneCommande1.getUpdateAt());

        assertThat(ligneCommande1).isEqualTo(ligneCommande2);
        ligneCommande2.setId(2L);
        assertThat(ligneCommande1).isNotEqualTo(ligneCommande2);
        ligneCommande1.setId(null);
        assertThat(ligneCommande1).isNotEqualTo(ligneCommande2);
        assertThat(ligneCommande1.getQuantite() + 1).isEqualTo(ligneCommande2.getQuantite());
        assertThat(ligneCommande1.getMontant()).isEqualTo(ligneCommande2.getMontant());
        assertThat(ligneCommande1.getValidated()).isNotEqualTo(ligneCommande2.getValidated());
        assertThat(ligneCommande1.getNomParticipant()).isEqualTo(ligneCommande2.getNomParticipant());
        assertThat(ligneCommande1.getProduit()).isNotEqualTo(ligneCommande2.getProduit());
        assertThat(ligneCommande1.getCommande()).isNotEqualTo(ligneCommande2.getCommande());
    }
}
