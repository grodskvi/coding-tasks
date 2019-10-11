package tasks.transferservice.rest.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.transferservice.domain.exception.InvalidTransferException;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.domain.rest.ExecuteTransferResponse;
import tasks.transferservice.service.TransferService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/transfer")
public class TransferResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransferResource.class);

    @Inject
    private TransferService transferService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ExecuteTransferResponse executeTransfer(ExecuteTransferRequest transferRequest) throws InvalidTransferException {
        LOG.info("Received request {}", transferRequest);

        //TODO: add request validation
        transferService.transfer(transferRequest);

        ExecuteTransferResponse response = new ExecuteTransferResponse();
        response.setTransferStatus("ACCEPTED");

        return response;
    }
}
