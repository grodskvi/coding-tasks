package tasks.transferservice.rest.resources;

import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.InputDataValidationException;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.CreateAccountResponse;
import tasks.transferservice.domain.rest.DepositRequest;
import tasks.transferservice.domain.rest.DepositResponse;
import tasks.transferservice.service.AccountService;
import tasks.transferservice.validation.CreateAccountRequestValidator;
import tasks.transferservice.validation.DepositRequestValidator;

@Path("/api/account")
public class AccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private CreateAccountRequestValidator createAccountRequestValidator;
    @Inject
    private DepositRequestValidator depositRequestValidator;

    @Inject
    private AccountService accountService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        LOG.debug("Received request to create account {}", request);

        List<String> errors = createAccountRequestValidator.validate(request);
        if (!errors.isEmpty()) {
            LOG.info("Validation of createAccountRequest {} failed with errors: {}", request, errors);
            throw new InputDataValidationException(
                    "Validation of createAccountRequest failed with errors " + String.join("\n", errors),
                    request.getRequestId(),
                    "createAccountRequest");
        }
        Account account = accountService.createAccount(request);

        CreateAccountResponse response = new CreateAccountResponse();
        AccountNumber accountNumber = account.getAccountNumber();
        response.setAccountNumber(accountNumber.getValue());

        return response;
    }

    @POST
    @Path("{accountNumber}/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DepositResponse deposit(@PathParam("accountNumber") String accountNumber, DepositRequest depositRequest) {
        List<String> errors = depositRequestValidator.validate(accountNumber, depositRequest);
        if (!errors.isEmpty()) {
            LOG.info("Validation of depositRequest {} failed with errors: {}", depositRequest, errors);
            throw new InputDataValidationException(
                "Validation of depositRequest failed with errors " + String.join("\n", errors),
                depositRequest.getRequestId(),
                "depositRequest");
        }
        accountService.deposit(anAccountNumber(accountNumber), depositRequest);
        return new DepositResponse("PROCESSED");
    }
}
