package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zergatstage.model.Questions;

/**
 * @author father
 */
public interface UserAnswerRepository extends JpaRepository<Questions, Long> {
}