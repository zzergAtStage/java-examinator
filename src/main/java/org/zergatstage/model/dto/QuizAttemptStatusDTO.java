package org.zergatstage.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptStatusDTO {
    private UUID sessionId;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private int score;
}
