package task.searchengine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.searchengine.server.controller.dto.ErrorResponse;
import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.SearchQuery;
import task.searchengine.server.domain.exception.DocumentNotFoundException;
import task.searchengine.server.domain.exception.DocumentProcessingException;
import task.searchengine.server.service.document.DocumentService;

import java.util.List;

@RestController
public class DocumentsController {

    @Autowired
    private DocumentService documentService;

    @GetMapping(path="document/{documentKey}")
    public Document getDocument(@PathVariable String documentKey) throws DocumentNotFoundException {
        DocumentKey lookupKey = new DocumentKey(documentKey);
        return documentService.getDocument(lookupKey);
    }

    @PostMapping(path="document/{documentKey}")
    public DocumentKey addDocument(@PathVariable String documentKey, @RequestBody String documentText) throws DocumentProcessingException {
        Document document = Document.aDocumentWithKey(documentKey, documentText);
        return documentService.addDocument(document);
    }

    @GetMapping(path = "documents/search")
    public List<DocumentKey> search(@RequestParam("token") List<String> searchTokens) {
        SearchQuery searchQuery = new SearchQuery(searchTokens);
        return documentService.search(searchQuery);
    }


    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(DocumentNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(DocumentProcessingException.class)
    public ResponseEntity<ErrorResponse> handleException(DocumentProcessingException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

}
