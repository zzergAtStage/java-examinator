package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author father
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JavaQuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private QuestionType questionType;
    private String questionHeader;
    private String questionText;
    private int difficultyLevel;
    private String correctAnswer;
    private String correctAnswerExplanation;
    private int points;

    @ElementCollection
    private List<String> choices; // Possible choices for the question

}
