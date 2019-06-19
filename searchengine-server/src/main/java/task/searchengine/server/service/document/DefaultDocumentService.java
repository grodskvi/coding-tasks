package task.searchengine.server.service.document;

import task.searchengine.server.domain.*;
import task.searchengine.server.repository.DocumentRepository;
import task.searchengine.server.service.search.SearchEngine;

import java.util.List;

import static java.lang.String.format;

public class DefaultDocumentService implements DocumentService {

    private final DocumentRepository documentRepository;
    private final SearchEngine searchEngine;

    public DefaultDocumentService(DocumentRepository documentRepository, SearchEngine searchEngine) {
        this.documentRepository = documentRepository;
        this.searchEngine = searchEngine;
    }

    @Override
    public void addDocument(Document document) {
        if (documentRepository.contains(document.getDocumentKey())) {
            //TODO: add logging
            String message = format("Document with key %s already exists", document.getDocumentKey());
            throw new DuplicateDocumentException(message);
        }
        documentRepository.save(document);
        searchEngine.index(document);
    }

    @Override
    public Document getDocument(DocumentKey documentKey) {
        return documentRepository.findBy(documentKey);
    }

    @Override
    public List<DocumentKey> search(SearchQuery searchQuery) {
        return searchEngine.search(searchQuery);
    }
}
