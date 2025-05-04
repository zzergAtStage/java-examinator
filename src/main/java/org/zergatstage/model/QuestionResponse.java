package org.zergatstage.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionResponse.
 */
@Entity
@Table(name = "question_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionResponse implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        @Column(name = "id")
        private Long id;

        @Size(max = 2048)
        @Column(name = "free_text_answer", length = 2048)
        private String freeTextAnswer;

        @Column(name = "awarded_points")
        private Integer awardedPoints;

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonIgnoreProperties(value = { "user", "quiz" }, allowSetters = true)
        private QuizAttempt attempt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonIgnoreProperties(value = { "section" }, allowSetters = true)
        private Question question;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "rel_question_response__options",
                joinColumns = @JoinColumn(name = "question_response_id"),
                inverseJoinColumns = @JoinColumn(name = "options_id")
        )
        @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
        @JsonIgnoreProperties(value = { "question", "responses" }, allowSetters = true)
        private Set<Option> options = new HashSet<>();

        // jhipster-needle-entity-add-field - JHipster will add fields here

        public Long getId() {
                return this.id;
        }

        public QuestionResponse id(Long id) {
                this.setId(id);
                return this;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getFreeTextAnswer() {
                return this.freeTextAnswer;
        }

        public QuestionResponse freeTextAnswer(String freeTextAnswer) {
                this.setFreeTextAnswer(freeTextAnswer);
                return this;
        }

        public void setFreeTextAnswer(String freeTextAnswer) {
                this.freeTextAnswer = freeTextAnswer;
        }

        public Integer getAwardedPoints() {
                return this.awardedPoints;
        }

        public QuestionResponse awardedPoints(Integer awardedPoints) {
                this.setAwardedPoints(awardedPoints);
                return this;
        }

        public void setAwardedPoints(Integer awardedPoints) {
                this.awardedPoints = awardedPoints;
        }

        public QuizAttempt getAttempt() {
                return this.attempt;
        }

        public void setAttempt(QuizAttempt quizAttempt) {
                this.attempt = quizAttempt;
        }

        public QuestionResponse attempt(QuizAttempt quizAttempt) {
                this.setAttempt(quizAttempt);
                return this;
        }

        public Question getQuestion() {
                return this.question;
        }

        public void setQuestion(Question question) {
                this.question = question;
        }

        public QuestionResponse question(Question question) {
                this.setQuestion(question);
                return this;
        }

        public Set<Option> getOptions() {
                return this.options;
        }

        public void setOptions(Set<Option> options) {
                this.options = options;
        }

        public QuestionResponse options(Set<Option> options) {
                this.setOptions(options);
                return this;
        }

        public QuestionResponse addOptions(Option option) {
                this.options.add(option);
                return this;
        }

        public QuestionResponse removeOptions(Option option) {
                this.options.remove(option);
                return this;
        }

        // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof QuestionResponse)) {
                        return false;
                }
                return getId() != null && getId().equals(((QuestionResponse) o).getId());
        }

        @Override
        public int hashCode() {
                // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
                return getClass().hashCode();
        }

        // prettier-ignore
        @Override
        public String toString() {
                return "QuestionResponse{" +
                        "id=" + getId() +
                        ", freeTextAnswer='" + getFreeTextAnswer() + "'" +
                        ", awardedPoints=" + getAwardedPoints() +
                        "}";
        }
}
