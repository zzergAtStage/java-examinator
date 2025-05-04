package org.zergatstage.model;

import org.zergatstage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.zergatstage.model.samplefactory.OptionTestSamples.getOptionRandomSampleGenerator;
import static org.zergatstage.model.samplefactory.QuestionResponseTestSamples.getQuestionResponseRandomSampleGenerator;
import static org.zergatstage.model.samplefactory.QuestionResponseTestSamples.getQuestionResponseSample1;
import static org.zergatstage.model.samplefactory.QuestionResponseTestSamples.getQuestionResponseSample2;
import static org.zergatstage.model.samplefactory.QuestionTestSamples.getQuestionRandomSampleGenerator;
import static org.zergatstage.model.samplefactory.QuizAttemptTestSamples.getQuizAttemptRandomSampleGenerator;
import static org.assertj.core.api.Assertions.assertThat;

class QuestionResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionResponse.class);
        QuestionResponse questionResponse1 = getQuestionResponseSample1();
        QuestionResponse questionResponse2 = new QuestionResponse();
        assertThat(questionResponse1).isNotEqualTo(questionResponse2);

        questionResponse2.setId(questionResponse1.getId());
        assertThat(questionResponse1).isEqualTo(questionResponse2);

        questionResponse2 = getQuestionResponseSample2();
        assertThat(questionResponse1).isNotEqualTo(questionResponse2);
    }

    @Test
    void attemptTest() {
        QuestionResponse questionResponse = getQuestionResponseRandomSampleGenerator();
        QuizAttempt quizAttemptBack = getQuizAttemptRandomSampleGenerator();

        questionResponse.setAttempt(quizAttemptBack);
        assertThat(questionResponse.getAttempt()).isEqualTo(quizAttemptBack);

        questionResponse.attempt(null);
        assertThat(questionResponse.getAttempt()).isNull();
    }

    @Test
    void questionTest() {
        QuestionResponse questionResponse = getQuestionResponseRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        questionResponse.setQuestion(questionBack);
        assertThat(questionResponse.getQuestion()).isEqualTo(questionBack);

        questionResponse.question(null);
        assertThat(questionResponse.getQuestion()).isNull();
    }

    @Test
    void optionsTest() {
        QuestionResponse questionResponse = getQuestionResponseRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        questionResponse.addOptions(optionBack);
        assertThat(questionResponse.getOptions()).containsOnly(optionBack);

        questionResponse.removeOptions(optionBack);
        assertThat(questionResponse.getOptions()).doesNotContain(optionBack);

        questionResponse.options(new HashSet<>(Set.of(optionBack)));
        assertThat(questionResponse.getOptions()).containsOnly(optionBack);

        questionResponse.setOptions(new HashSet<>());
        assertThat(questionResponse.getOptions()).doesNotContain(optionBack);
    }
}
