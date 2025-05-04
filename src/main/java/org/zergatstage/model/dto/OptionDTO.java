package org.zergatstage.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.zzergatstage.allin.domain.Option} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer displayOrder;

    @NotNull
    @Size(max = 512)
    private String text;

    @NotNull
    private Boolean correct;

    private QuestionDTO question;

    private Set<QuestionResponseDTO> responses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public Set<QuestionResponseDTO> getResponses() {
        return responses;
    }

    public void setResponses(Set<QuestionResponseDTO> responses) {
        this.responses = responses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionDTO)) {
            return false;
        }

        OptionDTO optionDTO = (OptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, optionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionDTO{" +
            "id=" + getId() +
            ", displayOrder=" + getDisplayOrder() +
            ", text='" + getText() + "'" +
            ", correct='" + getCorrect() + "'" +
            ", question=" + getQuestion() +
            ", responses=" + getResponses() +
            "}";
    }
}
