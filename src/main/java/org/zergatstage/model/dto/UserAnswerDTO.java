package org.zergatstage.model.dto;

import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.zergatstage.model.AnswerFormat;

import java.util.List;

/**
 * @author father
 */
@Builder
@Data
public class UserAnswerDTO {
    private Long questionId;
    @Enumerated
    private AnswerFormat answerFormat;
    @Singular
    private List<ChoiceDTO> answers;
}
