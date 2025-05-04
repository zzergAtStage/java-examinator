package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * publishedAt = null means draft.
 * <p>Version is fixed after publish; older QuizAttempt rows therefore always match the right blueprint.</p>
 * @author father
 */
@Entity
@Data
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
    private List<Section> sections; // Sections of the exam (grouped by topic/difficulty)

}
