package org.zergatstage.model;

import org.junit.jupiter.api.Test;
import org.zergatstage.web.rest.TestUtil;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.zergatstage.model.samplefactory.OptionTestSamples.*;
import static org.zergatstage.model.samplefactory.QuestionResponseTestSamples.getQuestionResponseRandomSampleGenerator;
import static org.zergatstage.model.samplefactory.QuestionTestSamples.getQuestionRandomSampleGenerator;

class OptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Option.class);
        Option option1 = getOptionSample1();
        Option option2 = new Option();
        assertThat(option1).isNotEqualTo(option2);

        option2.setId(option1.getId());
        assertThat(option1).isEqualTo(option2);

        option2 = getOptionSample2();
        assertThat(option1).isNotEqualTo(option2);
    }

    @Test
    void questionTest() {
        Option option = getOptionRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        option.setQuestion(questionBack);
        assertThat(option.getQuestion()).isEqualTo(questionBack);

        option.question(null);
        assertThat(option.getQuestion()).isNull();
    }

    @Test
    void responsesTest() {
        Option option = getOptionRandomSampleGenerator();
        QuestionResponse questionResponseBack = getQuestionResponseRandomSampleGenerator();

        option.addResponses(questionResponseBack);
        assertThat(option.getResponses()).containsOnly(questionResponseBack);
        assertThat(questionResponseBack.getOptions()).containsOnly(option);

        option.removeResponses(questionResponseBack);
        assertThat(option.getResponses()).doesNotContain(questionResponseBack);
        assertThat(questionResponseBack.getOptions()).doesNotContain(option);

        option.responses(new HashSet<>(Set.of(questionResponseBack)));
        assertThat(option.getResponses()).containsOnly(questionResponseBack);
        assertThat(questionResponseBack.getOptions()).containsOnly(option);

        option.setResponses(new HashSet<>());
        assertThat(option.getResponses()).doesNotContain(questionResponseBack);
        assertThat(questionResponseBack.getOptions()).doesNotContain(option);
    }
}