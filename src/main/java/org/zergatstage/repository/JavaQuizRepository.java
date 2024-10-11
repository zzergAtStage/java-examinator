package org.zergatstage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zergatstage.model.JavaQuizQuestion;

import java.util.List;

/**
 * @author father
 */
public interface JavaQuizRepository extends JpaRepository<JavaQuizQuestion, Long> {
    // Fetch questions by difficulty level
    List<JavaQuizQuestion> findByDifficultyLevel(int level);
    // Get all questions that are not harder than the specified difficulty
    List<JavaQuizQuestion> findByDifficultyLevelLessThanEqual(int difficulty);
    @Query("SELECT q FROM JavaQuizQuestion q JOIN FETCH q.choices")
    List<JavaQuizQuestion> findAllWithChoices();

    List<JavaQuizQuestion> findByQuestionHeader(String header);
}
