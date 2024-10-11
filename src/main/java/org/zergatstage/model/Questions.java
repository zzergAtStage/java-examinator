package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author father
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private JavaQuizQuestion question;
    @ElementCollection(fetch = FetchType.EAGER)
    @Singular
    private List<String> userAnswers; // The answer submitted by the user
    private boolean correct; // If the answer was correct
    private int pointsAwarded; // Points for the question

}
