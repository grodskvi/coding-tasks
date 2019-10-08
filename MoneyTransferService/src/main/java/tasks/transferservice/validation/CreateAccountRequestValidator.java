package tasks.transferservice.validation;

import static java.lang.String.format;
import static tasks.transferservice.domain.common.Currency.ISO_CODE_VALIDATOR;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tasks.transferservice.domain.rest.CreateAccountRequest;

public class CreateAccountRequestValidator {

    public List<String> validate(CreateAccountRequest createAccountRequest) {
        List<String> errors = new ArrayList<>();
        if (StringUtils.isBlank(createAccountRequest.getAccountNumber())) {
            errors.add(format("Wrong accountNumber format: '%s'", createAccountRequest.getAccountNumber()));
        }
        if (! ISO_CODE_VALIDATOR.test(createAccountRequest.getCurrencyIsoCode())) {
            errors.add(format("Wrong currencyIsoCode format: '%s'", createAccountRequest.getCurrencyIsoCode()));
        }
        return errors;
    }
}
