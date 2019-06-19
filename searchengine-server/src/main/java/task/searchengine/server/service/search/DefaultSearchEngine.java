package task.searchengine.server.service.search;

import task.searchengine.server.domain.*;
import task.searchengine.server.repository.IndexRepository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static task.searchengine.server.domain.Token.normalizedToken;

public class DefaultSearchEngine implements SearchEngine {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private final IndexRepository indexRepository;
    private final IndexBuilder indexBuilder;
    private final SearchResultBuilderProvider resultBuilderFactory;

    public DefaultSearchEngine(
            IndexRepository indexRepository,
            IndexBuilder indexBuilder,
            SearchResultBuilderProvider resultBuilderFactory) {

        this.indexRepository = indexRepository;
        this.indexBuilder = indexBuilder;
        this.resultBuilderFactory = resultBuilderFactory;
    }

    @Override
    public IndexedDocument index(Document document) {
        writeLock.lock();
        try {
            IndexedDocument indexedDocument = indexBuilder.buildIndexFor(document);
            indexRepository.save(indexedDocument);
            return indexedDocument;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public List<DocumentKey> search(SearchQuery searchQuery) {
        readLock.lock();

        try {
            SearchResultBuilder builder = resultBuilderFactory.getSearchResultBuilder();
            for (String token : searchQuery.getTokens()) {
                Token normalizedToken = normalizedToken(token);
                Set<DocumentKey> tokenDocuments = indexRepository.findDocuments(normalizedToken);
                builder.acceptSearchResult(normalizedToken, tokenDocuments);
            }
            return builder.getResult();
        } finally {
            readLock.unlock();
        }
    }
}
