package task.searchengine.server.service.search;

import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.IndexedDocument;
import task.searchengine.server.domain.Token;

import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

public class WordsBasedIndexBuilder implements IndexBuilder {
    @Override
    public IndexedDocument buildIndexFor(Document document) {
        String[] documentWords = document.getText().split("\\W+");
        Set<Token> tokens = asList(documentWords).stream()
                .filter(token -> !token.isEmpty())
                .map(Token::normalizedToken)
                .collect(toSet());
        return new IndexedDocument(document.getDocumentKey(), tokens);
    }
}
