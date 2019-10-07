package tasks.transferservice.domain.rest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ExecuteTransferResponse {
    private String transferId;
    private String transferStatus;
}
