package tasks.transferservice.repository.exception;

public class AlreadyPersistedEntityException extends RuntimeException {
    public AlreadyPersistedEntityException(String message) {
        super(message);
    }
}
