package org.zergatstage.model.dto;

import lombok.*;

/**
 * @author father
 */

@Builder

public record ResponseDTO(String errorMessage, String businessMessage) {
}
