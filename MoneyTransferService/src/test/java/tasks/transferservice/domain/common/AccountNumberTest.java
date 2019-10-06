package tasks.transferservice.domain.common;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;

public class AccountNumberTest {
    @Test
    public void createsAccountNumber() {
        AccountNumber accountNumber = anAccountNumber("11112222");
        assertThat(accountNumber.getAccountNumber()).isEqualTo("11112222");
    }

    @Test
    public void failsToCreateNullDomainKey() {
        assertThatThrownBy(() -> anAccountNumber(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create accountNumber with number 'null'");
    }

    @Test
    public void failsToCreateDomainKeyWithBlankId() {
        assertThatThrownBy(() -> anAccountNumber("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create accountNumber with number '   '");
    }
}