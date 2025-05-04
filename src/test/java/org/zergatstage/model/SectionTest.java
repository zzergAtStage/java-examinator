package org.zergatstage.model;

import org.junit.jupiter.api.Test;
import org.zergatstage.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.zergatstage.model.samplefactory.QuizTestSamples.getQuizRandomSampleGenerator;
import static org.zergatstage.model.samplefactory.SectionTestSamples.*;

class SectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Section.class);
        Section section1 = getSectionSample1();
        Section section2 = new Section();
        assertThat(section1).isNotEqualTo(section2);

        section2.setId(section1.getId());
        assertThat(section1).isEqualTo(section2);

        section2 = getSectionSample2();
        assertThat(section1).isNotEqualTo(section2);
    }

    @Test
    void quizTest() {
        Section section = getSectionRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        section.setQuiz(quizBack);
        assertThat(section.getQuiz()).isEqualTo(quizBack);

        section.quiz(null);
        assertThat(section.getQuiz()).isNull();
    }
}
