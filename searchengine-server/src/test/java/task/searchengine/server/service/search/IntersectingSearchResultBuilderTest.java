package task.searchengine.server.service.search;

import org.junit.Test;
import task.searchengine.server.domain.DocumentKey;
import task.searchengine.server.domain.Token;

import java.util.Collections;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static task.searchengine.utils.CollectionUtils.hashSet;

public class IntersectingSearchResultBuilderTest {

    private static final DocumentKey KEY_A = new DocumentKey("key a");
    private static final DocumentKey KEY_B = new DocumentKey("key b");
    private static final DocumentKey KEY_C = new DocumentKey("key c");

    private IntersectingSearchResultBuilder resultBuilder = new IntersectingSearchResultBuilder();

    @Test
    public void isEmptyAfterInitialization() {
        assertThat(resultBuilder.getResult()).isEmpty();
    }

    @Test
    public void containsAllElementsOfFirstResult() {
        resultBuilder.acceptSearchResult(new Token("a"), hashSet(KEY_A, KEY_B));

        assertThat(resultBuilder.getResult()).containsOnly(KEY_A, KEY_B);
    }
    @Test
    public void intersectsElementsOfDifferentTokens() {
        resultBuilder.acceptSearchResult(new Token("a"), hashSet(KEY_A, KEY_B));
        resultBuilder.acceptSearchResult(new Token("b"), hashSet(KEY_A, KEY_C));

        assertThat(resultBuilder.getResult()).containsExactly(KEY_A);
    }

    @Test
    public void resolvesEmptyTotalResultIfOneOfTokensAreEmpty() {
        resultBuilder.acceptSearchResult(new Token("a"), emptySet());
        resultBuilder.acceptSearchResult(new Token("b"), hashSet(KEY_A, KEY_C));

        assertThat(resultBuilder.getResult()).isEmpty();
    }

    @Test
    public void resolvesEmptyTotalResultIfLastfTokensIsEmpty() {
        resultBuilder.acceptSearchResult(new Token("a"), hashSet(KEY_A, KEY_C));
        resultBuilder.acceptSearchResult(new Token("b"), emptySet());

        assertThat(resultBuilder.getResult()).isEmpty();
    }
}