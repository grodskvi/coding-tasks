package task.searchengine.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import task.searchengine.client.domain.Document;
import task.searchengine.client.domain.DocumentKey;
import task.searchengine.client.domain.ErrorResponse;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class SearchEngineClient {

    private final String serverUrl;
    private HttpClient httpClient = HttpClients.createDefault();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final PrintWriter writer;

    public SearchEngineClient(String host, int port, PrintWriter writer) {
        serverUrl = "http://" + host + ":" + port;
        this.writer = writer;
    }


    public void addDocument(String documentKey, String filePath) throws IOException {
        HttpPost httpPost = new HttpPost(serverUrl + "/document/"+ documentKey);

        List<String> content = IOUtils.readLines(new FileReader(filePath));
        httpPost.setEntity(new StringEntity(String.join("\n", content)));

        HttpResponse httpResponse = doHttpResponse(httpPost, "application/json");

        String response = EntityUtils.toString(httpResponse.getEntity());
        if (isSuccessful(httpResponse)) {
            DocumentKey document = objectMapper.readValue(response, DocumentKey.class);
            write("Added document with key: " + document.getKey());
        } else {
            ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
            write("Got error while adding document:");
            write("Error message: " + errorResponse.getErrorMessage());
        }
    }

    public void getDocument(String documentKey) throws IOException {
        HttpGet httpGet = new HttpGet(serverUrl + "/document/"+ documentKey);
        HttpResponse httpResponse = doHttpResponse(httpGet, "application/json");

        String response = EntityUtils.toString(httpResponse.getEntity());
        if (isSuccessful(httpResponse)) {
            Document document = objectMapper.readValue(response, Document.class);
            write("Received document by key: " + document.getDocumentKey().getKey());
            write("Text: \n" + document.getText());
        } else {
            ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
            write("Got error while retrieving document:");
            write("Error message: " + errorResponse.getErrorMessage());
        }

    }

    public void searchDocuments(List<String> keywords) throws IOException {
        String queryString = String
                .join("&", keywords.stream()
                .map(t -> "token=" + t).collect(toList()));

        HttpGet httpGet = new HttpGet(serverUrl + "/document/search?" + queryString);
        HttpResponse httpResponse = doHttpResponse(httpGet, "application/json");

        String response = EntityUtils.toString(httpResponse.getEntity());
        if (isSuccessful(httpResponse)) {
            TypeReference<List<DocumentKey>> documentKeysType = new TypeReference<List<DocumentKey>>() {};
            List<DocumentKey> documentKeys = objectMapper.readValue(response, documentKeysType);
            write("Found documents by keywords " + keywords + ": ");

            List<String> keys = documentKeys.stream().map(DocumentKey::getKey).collect(toList());
            String result = keys.isEmpty() ? "No documents found" : String.join("\n", keys);
            write(result);
        } else {
            ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
            write("Got error while searching: " + errorResponse.getErrorMessage());
        }
    }

    private HttpResponse doHttpResponse(HttpRequestBase httpMethod, String contentType) throws IOException {
        httpMethod.addHeader("Content-Type", contentType);
        return httpClient.execute(httpMethod);

    }

    private boolean isSuccessful(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode() == 200;
    }

    private void write(String message) {
        writer.println(message);
        writer.flush();
    }

    public String getServerUrl() {
        return serverUrl;
    }
}
