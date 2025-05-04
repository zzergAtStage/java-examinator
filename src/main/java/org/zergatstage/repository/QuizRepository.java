package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zergatstage.model.Exam;
import org.zergatstage.model.Quiz;
import org.zergatstage.model.User;

import java.util.List;

/**
 * @author father
 */
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
