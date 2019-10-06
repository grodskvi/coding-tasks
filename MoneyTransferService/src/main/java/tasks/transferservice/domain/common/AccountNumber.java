package tasks.transferservice.domain.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import tasks.transferservice.validation.Preconditions;

@EqualsAndHashCode
@ToString
public class AccountNumber {
    private final String accountNumber;

    private AccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public static AccountNumber anAccountNumber(String accountNumber) {
        Preconditions.check(
                accountNumber,
                id -> !StringUtils.isBlank(id),
                "Can not create accountNumber with number '%s'", accountNumber);

        return new AccountNumber(accountNumber);
    }
}
