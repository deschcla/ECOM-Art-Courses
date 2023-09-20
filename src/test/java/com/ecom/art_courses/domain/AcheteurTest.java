package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class AcheteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acheteur.class);
        Acheteur acheteur1 = new Acheteur();
        acheteur1.setId(1L);
        acheteur1.adresse("123 Rue X").dateNaiss(LocalDate.EPOCH).numTel("0600000000").createdAt(Instant.now()).updateAt(Instant.now());

        Acheteur acheteur2 = new Acheteur();
        acheteur2.setId(acheteur1.getId());
        assertThat(acheteur1).isEqualTo(acheteur2);
        acheteur2.setId(2L);
        assertThat(acheteur1).isNotEqualTo(acheteur2);
        acheteur1.setId(null);
        assertThat(acheteur1).isNotEqualTo(acheteur2);
    }

    @Test
    void userIntegration() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = new User();
        user1.setId(1L);

        Acheteur acheteur3 = new Acheteur();
        acheteur3.id(3L);
        acheteur3.internalUser(user1);
        //assertThat(acheteur3.getInternalUserId()).isEqualTo(1L);

    }
}
