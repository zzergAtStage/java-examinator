package org.zergatstage.model;

import org.junit.jupiter.api.Test;
import org.zergatstage.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.zergatstage.model.samplefactory.UserTestSamples.getUserSample1;
import static org.zergatstage.model.samplefactory.UserTestSamples.getUserSample2;

public class UserTest {
    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User appUser1 = getUserSample1();
        User appUser2 = User.builder().build();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }
}
