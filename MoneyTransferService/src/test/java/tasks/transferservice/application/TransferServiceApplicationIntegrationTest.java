package tasks.transferservice.application;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tasks.transferservice.application.server.JettyServer;
import tasks.transferservice.domain.rest.AccountBalanceResponse;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.CreateAccountResponse;
import tasks.transferservice.domain.rest.DepositRequest;
import tasks.transferservice.domain.rest.ErrorResponse;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.domain.rest.ExecuteTransferResponse;

public class TransferServiceApplicationIntegrationTest {
    private JettyServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = TransferServiceApplication.startServer(12567);
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:12567/transfer-service/api/");
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void createsAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountNumber("11111");
        request.setCurrencyIsoCode("EUR");

        Response response = target.path("account").request()
            .post(Entity.json(request));

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(CreateAccountResponse.class))
            .extracting("accountNumber")
            .isEqualTo("11111");
    }

    @Test
    public void failsToCreateAccountWithoutAccountNumber() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountNumber("");
        request.setCurrencyIsoCode("EUR");

        Response response = target.path("account").request()
            .post(Entity.json(request));

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.readEntity(ErrorResponse.class))
            .extracting("message")
            .isNotNull();
    }

    @Test
    public void handlesTransferRequests() {
        createAccount("1111-2222", "EUR");
        createAccount("3333-4444", "EUR");

        depositToAccount("3333-4444", BigDecimal.valueOf(100));

        ExecuteTransferRequest transferRequest = new ExecuteTransferRequest();
        transferRequest.setCreditAccount("1111-2222");
        transferRequest.setDebitAccount("3333-4444");
        transferRequest.setTransferAmount(BigDecimal.valueOf(40));

        Response response = target.path("transfer").request()
                .post(Entity.json(transferRequest));

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(ExecuteTransferResponse.class))
            .extracting("transferStatus")
            .isEqualTo("ACCEPTED");

        response = target.path("account/1111-2222/balance").request().get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(AccountBalanceResponse.class)).extracting("balance").isEqualTo(BigDecimal.valueOf(40));

        response = target.path("account/3333-4444/balance").request().get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(AccountBalanceResponse.class)).extracting("balance").isEqualTo(BigDecimal.valueOf(60));
    }

    private void createAccount(String accountNumber, String currency) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountNumber(accountNumber);
        request.setCurrencyIsoCode(currency);

        Response response = target.path("account").request()
            .post(Entity.json(request));
        assertThat(response.getStatus()).isEqualTo(200);
    }

    private void depositToAccount(String accountNumber, BigDecimal amount) {
        DepositRequest depositRequest = new DepositRequest(UUID.randomUUID().toString(), amount);

        Response response = target.path(format("account/%s/deposit", accountNumber)).request()
            .post(Entity.json(depositRequest));
        assertThat(response.getStatus()).isEqualTo(200);
    }

}