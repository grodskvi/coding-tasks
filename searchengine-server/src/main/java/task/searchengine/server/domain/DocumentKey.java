package task.searchengine.server.domain;

import java.util.Objects;

public class DocumentKey {
    private final String key;

    public DocumentKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentKey that = (DocumentKey) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "DocumentKey{" +
                "key='" + key + '\'' +
                '}';
    }
}
