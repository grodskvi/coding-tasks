package tasks.transferservice.domain.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor // required for json serialization
@Getter
@Setter
public class ErrorResponse {
    private String originalRequestId;
    private String requestType;
    private String message;
}
