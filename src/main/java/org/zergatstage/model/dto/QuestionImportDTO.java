package org.zergatstage.model.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.zergatstage.model.AnswerFormat;
import org.zergatstage.model.QuestionType;

import java.io.Serializable;

@Data
public class QuestionImportDTO implements Serializable {

    private Long id;

    @Lob
    private String stem;

    @NotNull
    private QuestionType renderType;

    @NotNull
    private AnswerFormat answerFormat;

    private Integer difficulty;

    @NotNull
    private Integer points;

    private SectionDTO section;


}
