package task.searchengine.server.service.search;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.IndexedDocument;
import task.searchengine.server.domain.SearchQuery;
import task.searchengine.server.repository.IndexRepository;

import java.util.List;

public class DefaultSearchEngine implements SearchEngine {

    private final IndexRepository indexRepository;
    private final IndexBuilder indexBuilder;

    public DefaultSearchEngine(IndexRepository indexRepository, IndexBuilder indexBuilder) {
        this.indexRepository = indexRepository;
        this.indexBuilder = indexBuilder;
    }

    @Override
    public IndexedDocument index(Document document) {
        IndexedDocument indexedDocument = indexBuilder.buildIndexFor(document);
        indexRepository.save(indexedDocument);
        return indexedDocument;
    }

    @Override
    public List<DocumentKey> search(SearchQuery searchQuery) {
        return null;
    }
}
