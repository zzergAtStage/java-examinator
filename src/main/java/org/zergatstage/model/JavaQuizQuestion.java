package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Lob
    private String questionText;
    private int difficultyLevel;
    // father 10.10.2024:13:45 implement multiple choices answer
    private AnswerType typeOfAnswer;
    @ElementCollection(fetch = FetchType.EAGER)
    @Singular
    private List<String> correctAnswers;
    @Lob
    private String correctAnswerExplanation;
    private int points;

    @ElementCollection(fetch = FetchType.EAGER)
    @Singular
    private List<String> choices; // Possible choices for the question

}
