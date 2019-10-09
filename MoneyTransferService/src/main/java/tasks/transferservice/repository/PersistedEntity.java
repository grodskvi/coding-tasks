package tasks.transferservice.repository;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

class PersistedEntity<T> {
    private T entity;
    private ReentrantLock lock;
    private Function<T, T> entityView;

    private PersistedEntity(T entity, ReentrantLock lock, Function<T, T> entityView) {
        this.entity = entity;
        this.lock = lock;
        this.entityView = entityView;
    }

    T getEntity() {
        return entityView.apply(entity);
    }

    void lockForUpdate() {
        lock.lock();
    }

    void unlock() {
        lock.unlock();
    }


    static <T extends Cloneable> PersistedEntity<T> persistedEntity(T entity, Function<T, T> entityView) {
        T copy = entityView.apply(entity);
        ReentrantLock lock = new ReentrantLock();
        return new PersistedEntity<>(copy, lock, entityView);
    }

    PersistedEntity<T> updateWith(T entity) {
        return new PersistedEntity<>(entityView.apply(entity), lock, entityView);
    }
}
