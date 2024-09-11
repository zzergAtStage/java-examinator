package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author father
 */
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long section_id;
    private String sectionName; // Could be "Topic1", "Easy", "Hard", etc.


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "section_id") // Adds section_id to UserAnswer table
    private List<UserAnswer> userAnswers;

}
