package org.zergatstage.DTO;

import lombok.*;

/**
 * @author father
 */

@Builder

public record ResponseDTO(String errorMessage, String businessMessage) {
}
