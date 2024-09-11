package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author father
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private JavaQuizQuestion question;

    private String userAnswer; // The answer submitted by the user
    private boolean correct; // If the answer was correct
    private int pointsAwarded; // Points for the question

}
