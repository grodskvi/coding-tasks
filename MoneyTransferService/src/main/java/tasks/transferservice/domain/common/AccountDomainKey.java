package tasks.transferservice.domain.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import tasks.transferservice.validation.Preconditions;

@EqualsAndHashCode
@ToString
public class AccountDomainKey {
    private final String accountId;

    private AccountDomainKey(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public static AccountDomainKey anAccountDomainKey(String accountId) {
        Preconditions.check(
                accountId,
                id -> !StringUtils.isBlank(id),
                "Can not create accountDomainKey with id '%s'", accountId);

        return new AccountDomainKey(accountId);
    }
}
