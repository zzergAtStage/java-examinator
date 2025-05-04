package org.zergatstage.model.mappers;


import org.zergatstage.model.dto.*;
import org.zergatstage.model.*;
import org.mapstruct.*;
import java.util.UUID;

/**
 * MapStruct mapper to convert entities → DTOs used by the REST layer.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuizMapper {

    /**
     * Converts a Quiz entity into a QuizDTO (omitting all server-only fields).
     * @param quiz the JPA entity
     * @return populated QuizDTO
     */
    @Mapping(target = "quizId",      source = "id")
    @Mapping(target = "sections",    source = "sections")
    QuizDTO toDto(Quiz quiz);

    /**
     * Converts a Section entity into SectionDTO.
     * @param section the JPA entity
     * @return populated SectionDTO
     */
    @Mapping(target = "sectionId",   source = "id")
    @Mapping(target = "displayOrder", source = "displayOrder")
    @Mapping(target = "questions",   source = "questions")
    SectionDTO toDto(Section section);

    /**
     * Converts a Question entity into QuestionDTO.
     * @param question the JPA entity
     * @return populated QuestionDTO
     */
    @Mapping(target = "questionId",  source = "id")
    @Mapping(target = "renderType",  source = "renderType")
    @Mapping(target = "answerFormat",source = "answerFormat")
    @Mapping(target = "choices",     source = "options")
    QuestionDTO toDto(Question question);

    /**
     * Converts an Option entity into ChoiceDTO (text only).
     * @param option the JPA entity
     * @return populated ChoiceDTO
     */
    @Mapping(target = "optionId",    source = "id")
    ChoiceDTO toDto(Option option);

    /**
     * Example: binding a QuizAttempt to a lightweight status DTO.
     * @param attempt the user’s quiz session
     * @return Partial DTO containing progress info
     */
    @Mapping(target = "sessionId",   source = "sessionId")
    @Mapping(target = "startedAt",   source = "startedAt")
    @Mapping(target = "finishedAt",  source = "finishedAt")
    @Mapping(target = "score",       source = "score")
    QuizAttemptStatusDTO toStatusDto(QuizAttempt attempt);
}