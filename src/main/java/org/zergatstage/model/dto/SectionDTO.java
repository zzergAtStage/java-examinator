package org.zergatstage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for a section within a quiz, grouping related questions.
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SectionDTO {
    private Long sectionId;
    private String name;
    private int displayOrder;
    private List<QuestionDTO> questions;
}
