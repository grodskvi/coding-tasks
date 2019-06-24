package task.searchengine.client.http;

public abstract class HttpResponse<ENT,ERR> {
    public abstract boolean isSuccessful();
    public abstract ENT getEntity();
    public abstract ERR getError();

    public static <ENT, ERR> HttpResponse<ENT, ERR> successfulResponse(ENT entity) {
        return new SuccessfulHttpResponse<>(entity);
    }

    public static <ENT, ERR> HttpResponse<ENT, ERR> errorResponse(ERR error) {
        return new ErrorHttpResponse<>(error);
    }
}
