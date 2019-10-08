package tasks.transferservice.domain.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import tasks.transferservice.validation.Preconditions;

@EqualsAndHashCode
@ToString
@Getter
public class AccountNumber {
    private final String value;

    private AccountNumber(String value) {
        this.value = value;
    }

    public static AccountNumber anAccountNumber(String accountNumber) {
        Preconditions.check(
                accountNumber,
                id -> !StringUtils.isBlank(id),
                "Can not create accountNumber with value '%s'", accountNumber);

        return new AccountNumber(accountNumber);
    }
}
