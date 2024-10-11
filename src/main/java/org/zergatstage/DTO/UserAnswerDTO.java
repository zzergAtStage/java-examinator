package org.zergatstage.DTO;

import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.zergatstage.model.AnswerType;

import java.util.List;

/**
 * @author father
 */
@Builder
@Data
public class UserAnswerDTO {
    private Long questionId;
    @Enumerated
    private AnswerType answerType;
    @Singular
    private List<String> answers;
}
