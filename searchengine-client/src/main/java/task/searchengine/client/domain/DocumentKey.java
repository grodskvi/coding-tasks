package task.searchengine.client.domain;

public class DocumentKey {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static DocumentKey documentKey(String key) {
        DocumentKey documentKey = new DocumentKey();
        documentKey.setKey(key);
        return documentKey;
    }
}
