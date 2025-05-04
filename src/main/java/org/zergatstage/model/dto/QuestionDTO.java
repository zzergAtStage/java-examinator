package org.zergatstage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for an individual question; correctness flags are omitted.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Long questionId;
    private String stem;
    private String renderType;
    private String answerFormat;
    private int points;
    private List<ChoiceDTO> choices;
}
