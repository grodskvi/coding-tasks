package tasks.transferservice.repository.exception;

import static java.lang.String.format;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityId, Class<?> entityClass) {
        super(format("Entity of %s with id %s can't be found", entityClass.getName(), entityId));
    }
}
