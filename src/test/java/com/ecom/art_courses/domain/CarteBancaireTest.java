package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class CarteBancaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarteBancaire.class);
        CarteBancaire carteBancaire1 = new CarteBancaire();
        carteBancaire1.id(1L)
                      .createdAt(Instant.now())
                      .refCarte("référence de test");

        CarteBancaire carteBancaire2 = new CarteBancaire();
        carteBancaire2.setId(carteBancaire1.getId());
        assertThat(carteBancaire1).isEqualTo(carteBancaire2);
        carteBancaire2.setId(2L);
        carteBancaire2.updateAt(Instant.now());
        assertThat(carteBancaire1).isNotEqualTo(carteBancaire2);
        carteBancaire1.setId(null);
        assertThat(carteBancaire1).isNotEqualTo(carteBancaire2);

        TestUtil.equalsVerifier(Commande.class);
        Commande commande = new Commande();
        commande.setId(1L);
        carteBancaire1.commande(commande);
        assertThat(carteBancaire1.getCommande()).isEqualTo(commande);
        assertThat(carteBancaire1.getCommandeId()).isEqualTo(1L);
    }
}
