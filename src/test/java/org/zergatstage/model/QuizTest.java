package org.zergatstage.model;

import org.junit.jupiter.api.Test;
import org.zergatstage.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.zergatstage.model.samplefactory.QuizTestSamples.*;
import static org.zergatstage.model.samplefactory.UserTestSamples.getUserRandomSampleGenerator;

class QuizTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quiz.class);
        Quiz quiz1 = getQuizSample1();
        Quiz quiz2 = Quiz.builder().build();
        assertThat(quiz1).isNotEqualTo(quiz2);

        quiz2.setId(quiz1.getId());
        assertThat(quiz1).isEqualTo(quiz2);

        quiz2 = getQuizSample2();
        assertThat(quiz1).isNotEqualTo(quiz2);
    }

    @Test
    void authorTest() {
        Quiz quiz = getQuizRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        quiz.setAuthor(userBack);
        assertThat(quiz.getAuthor()).isEqualTo(userBack);

        quiz.author(null);
        assertThat(quiz.getAuthor()).isNull();
    }
}