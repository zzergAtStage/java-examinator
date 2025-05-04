package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zergatstage.model.Section;

/**
 * @author father
 */
public interface SectionRepository extends JpaRepository<Section, Long> {
}