package com.ecom.art_courses.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.art_courses.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReleveFactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleveFacture.class);
        ReleveFacture releveFacture1 = new ReleveFacture();
        releveFacture1.setId(1L);
        ReleveFacture releveFacture2 = new ReleveFacture();
        releveFacture2.setId(releveFacture1.getId());
        assertThat(releveFacture1).isEqualTo(releveFacture2);
        releveFacture2.setId(2L);
        assertThat(releveFacture1).isNotEqualTo(releveFacture2);
        releveFacture1.setId(null);
        assertThat(releveFacture1).isNotEqualTo(releveFacture2);
    }
}
