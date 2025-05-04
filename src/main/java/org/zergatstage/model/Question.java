package org.zergatstage.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;
    @Column(name = "header", nullable = false)
    private String header;
    @Lob
    @Column(name = "stem", nullable = false)
    private String stem;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "render_type", nullable = false)
    private QuestionType questionType;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "answer_format", nullable = false)
    private AnswerFormat answerFormat;
    @Column(name = "difficulty")
    private Integer difficulty;
    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"quiz"}, allowSetters = true)
    private Section section;

    public Question header(String header){
        this.header = header;
        return this;
    }
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public String getStem() {
        return this.stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public Question stem(String stem) {
        this.setStem(stem);
        return this;
    }

    public QuestionType getQuestionType() {
        return this.questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Question questionType(QuestionType renderType) {
        this.setQuestionType(renderType);
        return this;
    }

    public AnswerFormat getAnswerFormat() {
        return this.answerFormat;
    }

    public void setAnswerFormat(AnswerFormat answerFormat) {
        this.answerFormat = answerFormat;
    }

    public Question answerFormat(AnswerFormat answerFormat) {
        this.setAnswerFormat(answerFormat);
        return this;
    }

    public Integer getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Question difficulty(Integer difficulty) {
        this.setDifficulty(difficulty);
        return this;
    }

    public Integer getPoints() {
        return this.points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Question points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Question section(Section section) {
        this.setSection(section);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                ", stem='" + getStem() + "'" +
                ", renderType='" + getQuestionType() + "'" +
                ", answerFormat='" + getAnswerFormat() + "'" +
                ", difficulty=" + getDifficulty() +
                ", points=" + getPoints() +
                "}";
    }
}
