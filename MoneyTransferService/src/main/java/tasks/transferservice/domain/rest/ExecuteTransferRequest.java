package tasks.transferservice.domain.rest;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ExecuteTransferRequest {
    private String transferRequestId;
    private String transferRequestSource;
    private String creditAccount;
    private String debitAccount;
    private BigDecimal transferAmount;
}
