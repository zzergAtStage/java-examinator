package org.zergatstage.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Captures a user's interaction with a quiz, including
 * start/finish timestamps and score after auto‑grading.
 *
 * @author father
 */
@Table(name = "quiz_attempt",
        indexes = {
                @Index(name = "idx_attempt_user", columnList = "user_id"),
                @Index(name = "idx_attempt_quiz", columnList = "quiz_id"),
                @Index(name = "uk_attempt_sessionId", columnList = "sessionId", unique = true)
        }
)
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizAttempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "session_id", length = 36, nullable = false, unique = true)
    private UUID sessionId;

    @NotNull
    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "score")
    private Integer score;

    @Column(name = "max_score")
    private Integer maxScore;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"author"}, allowSetters = true)
    private Quiz quiz;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizAttempt id(Long id) {
        this.setId(id);
        return this;
    }

    public UUID getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public QuizAttempt sessionId(UUID sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public QuizAttempt startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public Instant getFinishedAt() {
        return this.finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public QuizAttempt finishedAt(Instant finishedAt) {
        this.setFinishedAt(finishedAt);
        return this;
    }

    public Integer getScore() {
        return this.score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public QuizAttempt score(Integer score) {
        this.setScore(score);
        return this;
    }

    public Integer getMaxScore() {
        return this.maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public QuizAttempt maxScore(Integer maxScore) {
        this.setMaxScore(maxScore);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User appUser) {
        this.user = appUser;
    }

    public QuizAttempt user(User appUser) {
        this.setUser(appUser);
        return this;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public QuizAttempt quiz(Quiz quiz) {
        this.setQuiz(quiz);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizAttempt)) {
            return false;
        }
        return getId() != null && getId().equals(((QuizAttempt) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizAttempt{" +
                "id=" + getId() +
                ", sessionId='" + getSessionId() + "'" +
                ", startedAt='" + getStartedAt() + "'" +
                ", finishedAt='" + getFinishedAt() + "'" +
                ", score=" + getScore() +
                ", maxScore=" + getMaxScore() +
                "}";
    }
}
