/**
 * Package containing Data Transfer Objects (DTOs) exposed to the client
 */
package org.zergatstage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor // Lombok generates a no-args constructor
@AllArgsConstructor // Lombok generates an all-args constructor
public class QuizDTO {

    private Long quizId;
    private String title;
    private String description;
    private List<SectionDTO> sections;
}

