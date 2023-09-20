package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReleveFactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleveFacture.class);

        // Creation of Commande for tests
        Commande commande1 = new Commande();
        Commande commande2 = new Commande();
        Set<Commande> commandesSet = new HashSet<Commande>();
        commandesSet.add(commande1);

        ReleveFacture releveFacture1 = new ReleveFacture();
        releveFacture1
            .id(1L)
            .montant((float) 20.00)
            .createdAt(Instant.now())
            .updateAt(Instant.now())
            .commandes(commandesSet)
            .addCommande(commande2);

        ReleveFacture releveFacture2 = new ReleveFacture();
        releveFacture2.setId(releveFacture1.getId());

        assertThat(releveFacture1).isEqualTo(releveFacture2);
        releveFacture2.setId(2L);
        assertThat(releveFacture1).isNotEqualTo(releveFacture2);
        releveFacture1.setId(null);
        assertThat(releveFacture1).isNotEqualTo(releveFacture2);
        assertThat(releveFacture1.getMontant()).isEqualTo((float) 20.00);
        releveFacture2.setMontant(releveFacture1.getMontant());
        assertThat(releveFacture2.getMontant()).isEqualTo((float) 20.00);
        releveFacture2.setCreatedAt(releveFacture1.getCreatedAt());
        releveFacture2.setUpdateAt(releveFacture1.getUpdateAt());
        releveFacture2.setCommandes(releveFacture1.getCommandes());
        assertThat(releveFacture1.getCommandes()).isEqualTo(releveFacture2.getCommandes());
        releveFacture1.removeCommande(commande1);
        releveFacture1.removeCommande(commande2);
        assertThat(releveFacture1.getCommandes().isEmpty());
        // releveFacture2.setAcheteur(releveFacture1.getAcheteur());

    }
}
