package tasks.transferservice.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import tasks.transferservice.domain.rest.CreateAccountRequest;

public class CreateAccountRequestValidatorTest {

    private CreateAccountRequestValidator validator = new CreateAccountRequestValidator();

    @Test
    public void validatesValidObject() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("request_id", "1111", "EUR");

        assertThat(validator.validate(createAccountRequest)).isEmpty();
    }

    @Test
    public void validatesNullAccountNumber() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("request_id", null, "EUR");

        assertThat(validator.validate(createAccountRequest))
            .containsExactly("Wrong accountNumber format: 'null'");
    }

    @Test
    public void validatesEmptyAccountNumber() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("request_id", "  ", "EUR");

        assertThat(validator.validate(createAccountRequest))
            .containsExactly("Wrong accountNumber format: '  '");
    }

    @Test
    public void validatesNullCurrencyCodes() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("request_id", "1111", null);

        assertThat(validator.validate(createAccountRequest))
            .containsExactly("Wrong currencyIsoCode format: 'null'");
    }

}