package task.searchengine.client.domain.event;


public class ClientStartedEvent {
    private final String serverUrl;

    public ClientStartedEvent(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }
}
