package org.zergatstage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author father
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Submissions {
    private int id;
    private LocalDateTime date;
    private String sessionIdToLink;
}
