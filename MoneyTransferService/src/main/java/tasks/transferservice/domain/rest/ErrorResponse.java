package tasks.transferservice.domain.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor // required for json serialization
@Getter
public class ErrorResponse {
    private String originalRequestId;
    private String requestType;
    private String message;
}
