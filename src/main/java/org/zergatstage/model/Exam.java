package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.*;
import org.zergatstage.DTO.UserAnswerDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author father
 */
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exam_id;
    private String sessionId; // Unique ID for this exam session
    private LocalDateTime examDate;

    @ManyToOne
    private User user; // The user taking the exam

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // One exam has many sections
    @JoinColumn(name = "exam_id") // Adds exam_id to ExamSection table
    private List<ExamSection> sections; // Sections of the exam (grouped by topic/difficulty)

}
