package tasks.transferservice.repository.exception;

import static java.lang.String.format;

public class EntityNotFoundException extends RuntimeException {
    private String entityId;
    private Class<?> entityClass;

    public EntityNotFoundException(String entityId, Class<?> entityClass) {
        super(format("Can not find entity of %s with id %s", entityClass.getName(), entityId));
        this.entityId = entityId;
        this.entityClass = entityClass;
    }
}
