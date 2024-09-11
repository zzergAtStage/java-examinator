package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zergatstage.model.ExamSection;

/**
 * @author father
 */
public interface ExamSectionRepository extends JpaRepository<ExamSection, Long> {
}