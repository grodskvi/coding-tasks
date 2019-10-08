package tasks.transferservice.rest.resources;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Currency.EUR;
import static tasks.transferservice.domain.entity.Account.anAccount;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.InputDataValidationException;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.CreateAccountResponse;
import tasks.transferservice.domain.rest.DepositRequest;
import tasks.transferservice.domain.rest.DepositResponse;
import tasks.transferservice.service.AccountService;
import tasks.transferservice.validation.CreateAccountRequestValidator;
import tasks.transferservice.validation.DepositRequestValidator;

@RunWith(MockitoJUnitRunner.class)
public class AccountResourceTest {

    @Mock
    private CreateAccountRequestValidator createAccountRequestValidator;
    @Mock
    private DepositRequestValidator depositRequestValidator;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountResource accountResource;

    @Test
    public void validatesRequestOnAccountCreation() {
        CreateAccountRequest request = new CreateAccountRequest("request_id", "1111", "EUR");
        when(createAccountRequestValidator.validate(request)).thenReturn(singletonList("Invalid request"));

        assertThatThrownBy(() -> accountResource.createAccount(request))
            .isInstanceOf(InputDataValidationException.class);
    }

    @Test
    public void handlesCreateAccountRequest() {
        CreateAccountRequest request = new CreateAccountRequest("request_id", "1111", "EUR");
        when(createAccountRequestValidator.validate(request)).thenReturn(emptyList());

        Account expectedAccount = anAccount("1111", EUR);
        when(accountService.createAccount(request)).thenReturn(expectedAccount);

        CreateAccountResponse createAccountResponse = accountResource.createAccount(request);
        assertThat(createAccountResponse.getAccountNumber()).isEqualTo("1111");
    }

    @Test
    public void handlesDeposit() {
        DepositRequest request = new DepositRequest("request_id", BigDecimal.TEN);
        DepositResponse response = accountResource.deposit("account_id", request);

        verify(accountService).deposit(anAccountNumber("account_id"), request);
        assertThat(response.getStatus()).isEqualTo("PROCESSED");
    }

}