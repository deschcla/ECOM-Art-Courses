package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class LigneCommandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCommande.class);
        LigneCommande ligneCommande1 = new LigneCommande();
        ligneCommande1.setId(1L);
        ligneCommande1.quantite(1).nomParticipant("participant").createdAt(Instant.now());
        LigneCommande ligneCommande2 = new LigneCommande();
        ligneCommande2.id(ligneCommande1.getId());
        assertThat(ligneCommande1).isEqualTo(ligneCommande2);
        ligneCommande2.setId(2L);
        assertThat(ligneCommande1).isNotEqualTo(ligneCommande2);
        ligneCommande1.setId(null);
        ligneCommande1.updateAt(Instant.now());
        assertThat(ligneCommande1).isNotEqualTo(ligneCommande2);
    }
}
