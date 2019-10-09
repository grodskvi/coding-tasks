package tasks.transferservice.repository.exception;

import static java.lang.String.format;

import tasks.transferservice.domain.entity.EntityKey;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(EntityKey entityKey) {
        super(format(
            "Can not find entity of %s with id %s",
            entityKey.getEntityClass().getName(),
            entityKey.getEntityId()));
    }
}
