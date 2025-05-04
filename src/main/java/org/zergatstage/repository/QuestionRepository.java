package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zergatstage.model.Question;

import java.util.List;

/**
 * @author father
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
