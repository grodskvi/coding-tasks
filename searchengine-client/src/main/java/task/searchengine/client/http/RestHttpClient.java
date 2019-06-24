package task.searchengine.client.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import task.searchengine.client.domain.exception.JsonParseException;

import java.io.IOException;
import java.util.List;

import static task.searchengine.client.http.HttpResponse.errorResponse;
import static task.searchengine.client.http.HttpResponse.successfulResponse;

public class RestHttpClient {
    private RestTemplate restTemplate = new RestTemplate();
    private final String serverUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestHttpClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public <ENT, ERR> HttpResponse<ENT, ERR> doGet(String endPoint, MediaType contentType, Class<ENT> entity, Class<ERR> error) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", contentType.toString());
        try {
            ResponseEntity<ENT> responseEntity = restTemplate.exchange(
                    serverUrl + endPoint,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    entity);
            return successfulResponse(responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            ERR errorResponse = parseResponseBody(error, e.getResponseBodyAsString());
            return errorResponse(errorResponse);
        }
    }

    public <ENT, ERR> HttpResponse<List<ENT>, ERR> doGetList(
            String endPoint,
            MediaType contentType,
            //TODO: replace spring abstraction in method API
            ParameterizedTypeReference<List<ENT>> entity,
            Class<ERR> error) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", contentType.toString());
        try {
            ResponseEntity<List<ENT>> responseEntity = restTemplate.exchange(
                    serverUrl + endPoint,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    entity);
            return successfulResponse(responseEntity.getBody());

        } catch (HttpStatusCodeException e) {
            ERR errorResponse = parseResponseBody(error, e.getResponseBodyAsString());
            return errorResponse(errorResponse);
        }
    }

    public <R, ENT, ERR> HttpResponse<ENT, ERR> doPost(String endPoint, MediaType contentType, R requestBody, Class<ENT> entity, Class<ERR> error) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", contentType.toString());
        try {
            HttpEntity<R> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<ENT> responseEntity = restTemplate.exchange(
                    serverUrl + endPoint,
                    HttpMethod.POST,
                    requestEntity,
                    entity);
            return successfulResponse(responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            ERR errorResponse = parseResponseBody(error, e.getResponseBodyAsString());
            return errorResponse(errorResponse);
        }
    }



    private <ERR> ERR parseResponseBody(Class<ERR> error, String response) {
        try {
            return objectMapper.readValue(response, error);
        } catch (IOException e) {
            throw new JsonParseException("Failed to parse json in " + response, e);
        }
    }
}
