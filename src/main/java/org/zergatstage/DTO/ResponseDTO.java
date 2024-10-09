package org.zergatstage.DTO;

import lombok.*;

/**
 * @author father
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
  private String errorMessage;
  private String businessMessage ;
}
