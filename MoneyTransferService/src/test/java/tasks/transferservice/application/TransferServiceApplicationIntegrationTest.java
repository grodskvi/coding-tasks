package tasks.transferservice.application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tasks.transferservice.application.server.JettyServer;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.domain.rest.ExecuteTransferResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
    public void tearDown() throws Exception {
        server.stop();
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