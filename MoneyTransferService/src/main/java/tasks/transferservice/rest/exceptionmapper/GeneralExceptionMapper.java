package tasks.transferservice.rest.exceptionmapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.rest.ErrorResponse;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger LOG = LoggerFactory.getLogger(InvalidDataExceptionMapper.class);

    @Override
    public Response toResponse(Exception e) {
        LOG.info("Converting exception to response ", e);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(errorResponse)
            .build();
    }
}
