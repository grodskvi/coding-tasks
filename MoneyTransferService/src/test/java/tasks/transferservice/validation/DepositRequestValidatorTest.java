package tasks.transferservice.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import tasks.transferservice.domain.rest.DepositRequest;

public class DepositRequestValidatorTest {

    private DepositRequestValidator validator = new DepositRequestValidator();

    @Test
    public void preventsDepositWithUndefinedAmount() {
        DepositRequest request = new DepositRequest("request_id", null);
        List<String> errors = validator.validate("account_id", request);
        assertThat(errors).containsExactly("Invalid amount 'null'");
    }

    @Test
    public void preventsDepositWithZeroAmount() {
        DepositRequest request = new DepositRequest("request_id", BigDecimal.ZERO);
        List<String> errors = validator.validate("account_id", request);
        assertThat(errors).containsExactly("Invalid amount '0'");
    }

    @Test
    public void preventsDepositWithNegativeAmount() {
        DepositRequest request = new DepositRequest("request_id", BigDecimal.valueOf(-1));
        List<String> errors = validator.validate("account_id", request);
        assertThat(errors).containsExactly("Invalid amount '-1'");
    }

    @Test
    public void preventsDepositWithoutAccountNumber() {
        DepositRequest request = new DepositRequest("request_id", BigDecimal.valueOf(10));
        List<String> errors = validator.validate(" ", request);
        assertThat(errors).containsExactly("Invalid accountNumber ' '");
    }

    @Test
    public void indicatesValidDepositRequest() {
        DepositRequest request = new DepositRequest("request_id", BigDecimal.valueOf(10));
        List<String> errors = validator.validate("account_id", request);
        assertThat(errors).isEmpty();
    }

}