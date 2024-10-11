package org.zergatstage.DTO;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.zergatstage.model.Exam;
import org.zergatstage.model.ExamSection;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author father
 */
@Data
@Component
public class ExamSubmissionDTO {
    private String sessionId;
    private Long userId;
    private Map<String, List<UserAnswerDTO>> sectionAnswers; // Section name as key, list of answers as value

}
