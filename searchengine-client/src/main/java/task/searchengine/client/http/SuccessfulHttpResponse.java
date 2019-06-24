package task.searchengine.client.http;

class SuccessfulHttpResponse<ENT, ERR> extends HttpResponse<ENT, ERR> {

    private final ENT entity;

    SuccessfulHttpResponse(ENT entity) {
        this.entity = entity;
    }

    @Override
    public boolean isSuccessful() {
        return true;
    }

    @Override
    public ENT getEntity() {
        return entity;
    }

    @Override
    public ERR getError() {
        throw new UnsupportedOperationException("Successful response does not contain error");
    }
}
