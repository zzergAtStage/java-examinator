package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zergatstage.model.QuizAttempt;

/**
 * @author father
 */
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
}