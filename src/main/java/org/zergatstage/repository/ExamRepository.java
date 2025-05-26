package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zergatstage.model.Exam;
import org.zergatstage.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @author father
 */
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findBySessionId(String sessionId);
    List<Exam> findByUser(User user);
}
