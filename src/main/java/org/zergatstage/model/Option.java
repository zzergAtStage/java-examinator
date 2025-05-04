package org.zergatstage.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * an Option.
 */
@Getter
@Entity
@Table(name = "jhi_option")
@SuperBuilder
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Option extends AuditableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    //NOTE: experimental mix of JH and chat superclass - id

    @NotNull
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @NotNull
    @Size(max = 512)
    @Column(name = "text", length = 512, nullable = false)
    private String text;

    @NotNull
    @Column(name = "correct", nullable = false)
    private Boolean correct;

    @Column(name = "correct_answer_explanation")
    private String explanation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"section"}, allowSetters = true)
    private Question question;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "options")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"attempt", "question", "options"}, allowSetters = true)
    private Set<QuestionResponse> responses = new HashSet<>();


    public void setId(Long id) {
        this.id = id;
    }

    public Option id(Long id) {
        this.setId(id);
        return this;
    }


    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Option displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Option text(String text) {
        this.setText(text);
        return this;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Option correct(Boolean correct) {
        this.setCorrect(correct);
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Option question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public void setResponses(Set<QuestionResponse> questionResponses) {
        if (this.responses != null) {
            this.responses.forEach(i -> i.removeOptions(this));
        }
        if (questionResponses != null) {
            questionResponses.forEach(i -> i.addOptions(this));
        }
        this.responses = questionResponses;
    }

    public Option responses(Set<QuestionResponse> questionResponses) {
        this.setResponses(questionResponses);
        return this;
    }

    public Option addResponses(QuestionResponse questionResponse) {
        this.responses.add(questionResponse);
        questionResponse.getOptions().add(this);
        return this;
    }

    public Option removeResponses(QuestionResponse questionResponse) {
        this.responses.remove(questionResponse);
        questionResponse.getOptions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return getId() != null && getId().equals(((Option) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Option{" +
                "id=" + getId() +
                ", displayOrder=" + getDisplayOrder() +
                ", text='" + getText() + "'" +
                ", correct='" + getCorrect() + "'" +
                "}";
    }
}
