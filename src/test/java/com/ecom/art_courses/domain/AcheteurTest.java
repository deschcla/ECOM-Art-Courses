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
        acheteur1.id(1L).createdAt(Instant.now()).adresse("123 avenue principal").dateNaiss(LocalDate.EPOCH).numTel("0600000000");

        Acheteur acheteur2 = new Acheteur();
        acheteur2.setId(acheteur1.getId());
        assertThat(acheteur1).isEqualTo(acheteur2);
        acheteur2.setId(2L);
        assertThat(acheteur1).isNotEqualTo(acheteur2);
        acheteur1.id(null).updateAt(Instant.now());
        assertThat(acheteur1).isNotEqualTo(acheteur2);
    }
}
