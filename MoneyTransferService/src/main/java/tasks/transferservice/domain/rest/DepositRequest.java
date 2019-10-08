package tasks.transferservice.domain.rest;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DepositRequest {
    private String requestId;
    private BigDecimal amount;
}
