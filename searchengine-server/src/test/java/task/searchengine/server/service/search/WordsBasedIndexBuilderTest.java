package task.searchengine.server.service.search;

import org.junit.Test;
import task.searchengine.server.domain.Document;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.IndexedDocument;

import static org.assertj.core.api.Assertions.assertThat;


public class WordsBasedIndexBuilderTest {

    private WordsBasedIndexBuilder indexBuilder = new WordsBasedIndexBuilder();


    @Test
    public void createsIndexedDocumentWithDocumentKey() {
        IndexedDocument indexedDocument = indexBuilder.buildIndexFor(Document.aDocumentWithKey("key", "some sample text to test"));

        assertThat(indexedDocument.getDocumentKey()).isEqualTo(new DocumentKey("key"));
    }

    @Test
    public void splitsTextBySpaces() {
        IndexedDocument indexedDocument = indexBuilder.buildIndexFor(Document.aDocumentWithKey("key", "some sample text to test"));

        assertThat(indexedDocument.getTokens())
                .extracting("token")
                .containsOnly("some", "sample", "text", "to", "test");
    }

    @Test
    public void mergesDuplicateWords() {
        IndexedDocument indexedDocument = indexBuilder.buildIndexFor(Document.aDocumentWithKey("key", "some sample text with duplicate text"));

        assertThat(indexedDocument.getTokens())
                .extracting("token")
                .containsOnly("some", "sample", "text", "with", "duplicate");
    }

    @Test
    public void ignoresPunctuation() {
        IndexedDocument indexedDocument = indexBuilder.buildIndexFor(Document.aDocumentWithKey("key", "This is sentence, however it has subsentence. Another one."));

        assertThat(indexedDocument.getTokens())
                .extracting("token")
                .containsOnly("this", "is", "sentence", "however", "it", "has", "subsentence", "another", "one");
    }

    @Test
    public void lowercasesEveryToken() {
        IndexedDocument indexedDocument = indexBuilder.buildIndexFor(Document.aDocumentWithKey("key", "CAPITALIZED phrase"));

        assertThat(indexedDocument.getTokens())
                .extracting("token")
                .containsOnly("capitalized", "phrase");
    }

    @Test
    public void acceptsNumbersInWords() {
        IndexedDocument indexedDocument = indexBuilder.buildIndexFor(Document.aDocumentWithKey("key", "{A4, 3b}"));

        assertThat(indexedDocument.getTokens())
                .extracting("token")
                .containsOnly("a4", "3b");
    }

}