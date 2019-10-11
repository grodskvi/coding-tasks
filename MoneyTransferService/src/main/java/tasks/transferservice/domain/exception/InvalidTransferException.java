package tasks.transferservice.domain.exception;

import tasks.transferservice.domain.rest.ExecuteTransferRequest;

public class InvalidTransferException extends Exception {
    private ExecuteTransferRequest source;

    public InvalidTransferException(ExecuteTransferRequest source, String message) {
        super(message);
        this.source = source;
    }

    public ExecuteTransferRequest getSource() {
        return source;
    }
}
