package org.zergatstage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for a selectable choice in an MCQ question.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceDTO {
    private Long optionId;
    private String text;

}
