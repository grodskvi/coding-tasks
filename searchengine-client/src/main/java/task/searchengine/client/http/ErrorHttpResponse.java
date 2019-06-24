package task.searchengine.client.http;

class ErrorHttpResponse<ENT, ERR> extends HttpResponse<ENT, ERR> {

    private final ERR error;

    ErrorHttpResponse(ERR error) {
        this.error = error;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    @Override
    public ENT getEntity() {
        throw new UnsupportedOperationException("Error response does not contain entity");
    }

    @Override
    public ERR getError() {
        return error;
    }
}
