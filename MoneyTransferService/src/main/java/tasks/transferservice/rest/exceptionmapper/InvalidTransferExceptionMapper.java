package tasks.transferservice.rest.exceptionmapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.exception.InvalidTransferException;
import tasks.transferservice.domain.rest.ErrorResponse;

@Provider
public class InvalidTransferExceptionMapper implements ExceptionMapper<InvalidTransferException> {

    private static final Logger LOG = LoggerFactory.getLogger(InvalidTransferExceptionMapper.class);

    @Override
    public Response toResponse(InvalidTransferException e) {
        LOG.info("Converting exception to response ", e);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setOriginalRequestId(e.getSource().getTransferRequestId());

        return Response.status(Response.Status.NOT_FOUND)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(errorResponse)
            .build();
    }
}
