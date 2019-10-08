package tasks.transferservice.domain.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class AccountOperation {
    private AccountOperationType operationType;
    private Amount amount;
    private Currency currency;

    private AccountOperation(AccountOperationType operationType, Amount amount, Currency currency) {
        this.operationType = operationType;
        this.amount = amount;
        this.currency = currency;
    }

    public static AccountOperation anAccountOperation(AccountOperationType operationType, Amount amount, Currency currency) {
        return new AccountOperation(operationType, amount, currency);
    }
}
