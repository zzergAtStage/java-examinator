package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zergatstage.model.Exam;
import org.zergatstage.model.User;

import java.util.List;

/**
 * @author father
 */
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Exam findBySessionId(String sessionId);
    List<Exam> findByUser(User user);
}
