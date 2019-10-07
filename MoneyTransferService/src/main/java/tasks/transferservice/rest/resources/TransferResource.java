package tasks.transferservice.rest.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.domain.rest.ExecuteTransferResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/transfer")
public class TransferResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransferResource.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ExecuteTransferResponse executeTransfer(ExecuteTransferRequest transferRequest) {
        LOG.info("Received request {}", transferRequest);

        ExecuteTransferResponse response = new ExecuteTransferResponse();
        response.setTransferId("111-222");
        response.setTransferStatus("ACCEPTED");
        return response;
    }
}
