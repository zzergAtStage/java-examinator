package org.zergatstage.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import static java.math.BigInteger.ZERO;

/**
 * @author father
 */
@Data
@RequiredArgsConstructor
public class SubmissionResult {
    private int totalScore;
    private int totalAnswered;
    private int totalFalseAnswered;
    private String submissionId;
    private String participantName;

    public void increaseTotalAnswered() {
        ++this.totalAnswered;
    }

    public void increaseTotalScore(int points) {
         if (points != 0) this.totalScore = this.totalScore + points;
         else ++this.totalScore;
    }

    public void increaseFalseAnswered() {
        ++this.totalFalseAnswered;
    }

}
