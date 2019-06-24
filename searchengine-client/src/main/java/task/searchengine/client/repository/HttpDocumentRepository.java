package task.searchengine.client.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import task.searchengine.client.domain.Document;
import task.searchengine.client.domain.DocumentKey;
import task.searchengine.client.domain.ErrorResponse;
import task.searchengine.client.domain.SearchQuery;
import task.searchengine.client.domain.exception.DocumentProcessingException;
import task.searchengine.client.domain.exception.DocumentRetrievalException;
import task.searchengine.client.domain.exception.SearchDocumentsException;
import task.searchengine.client.http.HttpResponse;
import task.searchengine.client.http.RestHttpClient;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class HttpDocumentRepository implements DocumentRepository {

    private final RestHttpClient restHttpClient;

    public HttpDocumentRepository(String serverUrl) {
        restHttpClient = new RestHttpClient(serverUrl);
    }

    @Override
    public Document getDocument(DocumentKey documentKey) throws DocumentRetrievalException {
        task.searchengine.client.http.HttpResponse<Document, ErrorResponse> response = restHttpClient.doGet(
                "/document/"+ documentKey.getKey(),
                MediaType.APPLICATION_JSON,
                Document.class,
                ErrorResponse.class);

        if (!response.isSuccessful()) {
            ErrorResponse error = response.getError();
            throw new DocumentRetrievalException(error.getErrorMessage());
        }
        return response.getEntity();
    }

    @Override
    public DocumentKey addDocument(Document document) throws DocumentProcessingException {
        task.searchengine.client.http.HttpResponse<DocumentKey, ErrorResponse> response = restHttpClient.doPost(
                "/document/" + document.getDocumentKey().getKey(),
                MediaType.TEXT_PLAIN,
                document.getText(),
                DocumentKey.class,
                ErrorResponse.class);

        if (!response.isSuccessful()) {
            ErrorResponse error = response.getError();
            throw new DocumentProcessingException(error.getErrorMessage());
        }
        return response.getEntity();
    }

    @Override
    public List<DocumentKey> searchDocuments(SearchQuery query) throws SearchDocumentsException {
        List<String> queryTokens = query.getKeywords().stream()
                .map(t -> "token=" + t)
                .collect(toList());

        String queryString = String.join("&", queryTokens);

        HttpResponse<List<DocumentKey>, ErrorResponse> response = restHttpClient.doGetList(
                "/document/search?" + queryString,
                MediaType.APPLICATION_JSON,
                new ParameterizedTypeReference<List<DocumentKey>>() {},
                ErrorResponse.class);

        if (!response.isSuccessful()) {
            ErrorResponse error = response.getError();
            throw new SearchDocumentsException(error.getErrorMessage());
        }
        return response.getEntity();
    }
}
