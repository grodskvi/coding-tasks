package tasks.transferservice.rest.resources;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.InputDataValidationException;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.CreateAccountResponse;
import tasks.transferservice.domain.rest.DepositRequest;
import tasks.transferservice.domain.rest.DepositResponse;
import tasks.transferservice.service.AccountService;
import tasks.transferservice.validation.CreateAccountRequestValidator;

@Path("/api/account")
public class AccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private CreateAccountRequestValidator createAccountRequestValidator;

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
        response.setAccountId(account.getDomainKey().getAccountId());

        return response;
    }

    @POST
    @Path("{accountId}/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DepositResponse deposit(@PathParam("accountId") String accountId, DepositRequest depositRequest) {
        BigDecimal amount = depositRequest.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InputDataValidationException(
                    "Validation of depositRequest failed because of unsupportable amount " + depositRequest.getAmount(),
                    depositRequest.getRequestId(),
                    "depositRequest");
        }
        accountService.deposit(accountId, depositRequest);
        return new DepositResponse("PROCESSED");
    }
}
