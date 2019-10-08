package tasks.transferservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tasks.transferservice.application.server.JettyServer;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.CreateAccountResponse;
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
        ExecuteTransferRequest transferRequest = new ExecuteTransferRequest();
        transferRequest.setTransferRequestId("request_id");
        transferRequest.setCreditAccount("11111");
        transferRequest.setDebitAccount("2222");
        transferRequest.setTransferAmount(BigDecimal.valueOf(50.09));

        ExecuteTransferResponse response = target.path("transfer").request()
                .post(Entity.json(transferRequest), ExecuteTransferResponse.class);
        assertThat(response.getTransferStatus()).isEqualTo("ACCEPTED");
    }

}