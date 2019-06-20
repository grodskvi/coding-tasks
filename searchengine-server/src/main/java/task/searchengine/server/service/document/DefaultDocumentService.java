package task.searchengine.server.service.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import task.searchengine.server.domain.*;
import task.searchengine.server.domain.exception.DocumentNotFoundException;
import task.searchengine.server.domain.exception.DuplicateDocumentException;
import task.searchengine.server.repository.DocumentRepository;
import task.searchengine.server.service.search.SearchEngine;

import java.util.List;

import static java.lang.String.format;

@Service
public class DefaultDocumentService implements DocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDocumentService.class);

    private final DocumentRepository documentRepository;
    private final SearchEngine searchEngine;

    public DefaultDocumentService(DocumentRepository documentRepository, SearchEngine searchEngine) {
        this.documentRepository = documentRepository;
        this.searchEngine = searchEngine;
    }

    @Override
    public DocumentKey addDocument(Document document) throws DuplicateDocumentException {
        if (documentRepository.contains(document.getDocumentKey())) {
            LOG.info("Attempted to add document with existing key {}", document.getDocumentKey());
            String message = format("Document with key %s already exists", document.getDocumentKey());
            throw new DuplicateDocumentException(message);
        }
        documentRepository.save(document);
        searchEngine.index(document);
        return document.getDocumentKey();
    }

    @Override
    public Document getDocument(DocumentKey documentKey) throws DocumentNotFoundException{
        return documentRepository
                .findBy(documentKey)
                .orElseThrow(() -> new DocumentNotFoundException("Can not find document with key: " + documentKey.getKey()));
    }

    @Override
    public List<DocumentKey> search(SearchQuery searchQuery) {
        return searchEngine.search(searchQuery);
    }
}
