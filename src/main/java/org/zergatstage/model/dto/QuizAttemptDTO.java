package org.zergatstage.model.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.zzergatstage.allin.domain.QuizAttempt} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizAttemptDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID sessionId;

    @NotNull
    private Instant startedAt;

    private Instant finishedAt;

    private Integer score;

    private Integer maxScore;

    private AppUserDTO user;

    private QuizDTO quiz;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public AppUserDTO getUser() {
        return user;
    }

    public void setUser(AppUserDTO user) {
        this.user = user;
    }

    public QuizDTO getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizDTO quiz) {
        this.quiz = quiz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizAttemptDTO)) {
            return false;
        }

        QuizAttemptDTO quizAttemptDTO = (QuizAttemptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizAttemptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizAttemptDTO{" +
            "id=" + getId() +
            ", sessionId='" + getSessionId() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            ", score=" + getScore() +
            ", maxScore=" + getMaxScore() +
            ", user=" + getUser() +
            ", quiz=" + getQuiz() +
            "}";
    }
}
