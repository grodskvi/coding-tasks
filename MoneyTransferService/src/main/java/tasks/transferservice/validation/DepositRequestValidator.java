package tasks.transferservice.validation;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jvnet.hk2.annotations.Service;

import tasks.transferservice.domain.rest.DepositRequest;

@Service
public class DepositRequestValidator {

    public List<String> validate(String accountNumber, DepositRequest depositRequest) {
        List<String> errors = new ArrayList<>();
        if (isBlank(accountNumber)) {
            errors.add(format("Invalid accountNumber '%s'", accountNumber));
        }
        BigDecimal amount = depositRequest.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(format("Invalid amount '%s'", amount));
        }
        return errors;
    }
}
