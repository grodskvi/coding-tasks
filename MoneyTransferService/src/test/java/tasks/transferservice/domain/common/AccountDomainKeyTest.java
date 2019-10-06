package tasks.transferservice.domain.common;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tasks.transferservice.domain.common.AccountDomainKey.anAccountDomainKey;


public class AccountDomainKeyTest {

    @Test
    public void createsDomainKey() {
        AccountDomainKey domainKey = anAccountDomainKey("account_id");
        assertThat(domainKey.getAccountId()).isEqualTo("account_id");
    }

    @Test
    public void failsToCreateNullDomainKey() {
        assertThatThrownBy(() -> anAccountDomainKey(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create accountDomainKey with id 'null'");
    }

    @Test
    public void failsToCreateDomainKeyWithBlankId() {
        assertThatThrownBy(() -> anAccountDomainKey("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create accountDomainKey with id '   '");
    }

}