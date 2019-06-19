package task.searchengine.server.service.search;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.IndexedDocument;

public interface IndexBuilder {
    IndexedDocument buildIndexFor(Document document);
}
