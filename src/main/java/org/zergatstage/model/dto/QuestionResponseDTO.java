package org.zergatstage.model.dto;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.zzergatstage.allin.domain.QuestionResponse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionResponseDTO implements Serializable {

    private Long id;

    @Size(max = 2048)
    private String freeTextAnswer;

    private Integer awardedPoints;

    private QuizAttemptDTO attempt;

    private QuestionDTO question;

    private Set<OptionDTO> options = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFreeTextAnswer() {
        return freeTextAnswer;
    }

    public void setFreeTextAnswer(String freeTextAnswer) {
        this.freeTextAnswer = freeTextAnswer;
    }

    public Integer getAwardedPoints() {
        return awardedPoints;
    }

    public void setAwardedPoints(Integer awardedPoints) {
        this.awardedPoints = awardedPoints;
    }

    public QuizAttemptDTO getAttempt() {
        return attempt;
    }

    public void setAttempt(QuizAttemptDTO attempt) {
        this.attempt = attempt;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public Set<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(Set<OptionDTO> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionResponseDTO)) {
            return false;
        }

        QuestionResponseDTO questionResponseDTO = (QuestionResponseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionResponseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionResponseDTO{" +
            "id=" + getId() +
            ", freeTextAnswer='" + getFreeTextAnswer() + "'" +
            ", awardedPoints=" + getAwardedPoints() +
            ", attempt=" + getAttempt() +
            ", question=" + getQuestion() +
            ", options=" + getOptions() +
            "}";
    }
}
