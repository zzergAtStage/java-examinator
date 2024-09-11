package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zergatstage.model.UserAnswer;

/**
 * @author father
 */
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
}