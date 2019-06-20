package task.searchengine.server.functional;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import task.searchengine.server.application.SearchEngineServerApplicationConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {SearchEngineServerApplicationConfiguration.class})
public class SearchEngineFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String KAFKA_TRANSFORMATION = "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections. The bedding was hardly able to cover it and seemed ready to slide off any moment. His many legs, pitifully thin compared with the size of the rest of him, waved about helplessly as he looked. \"What's happened to me?\" he thought. It wasn't a dream. His room, a proper human room although a little too small, lay peacefully between its four familiar walls.";
    private static final String EUROPEAN_LANGUAGES = "wonderful The European languages are members of the same family. Their separate existence is a myth. For science, music, sport, etc, Europe uses the same vocabulary. The languages only differ in their grammar, their pronunciation and their most common words. Everyone realizes why a new common language would be desirable: one could refuse to pay expensive translators. To achieve this, it would be necessary to have uniform grammar, pronunciation and more common words. If several languages coalesce, the grammar of the resulting language is more simple and regular than that of the individual languages. The new common language will be more simple and regular than the existing European languages. It will be as simple as Occidental; in fact, it will be Occidental. To an English person, it will seem like simplified English, as a skeptical Cambridge friend of mine told me what Occidental is.The European languages are members of the same family. Their separate existence is a myth.";
    private static final String WERTHER = "A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so happy, my dear friend, so absorbed in the exquisite sense of mere tranquil existence, that I neglect my talents. I should be incapable of drawing a single stroke at the present moment; and yet I feel that I never was a greater artist than now. When, while the lovely valley teems with vapour around me, and the meridian sun strikes the upper surface of the impenetrable foliage of my trees, and but a few stray gleams steal into the inner sanctuary, I throw myself down among the tall grass by the trickling stream; and, as I lie close to the earth, a thousand unknown plants are noticed by me: when I hear the buzz of the little world among the stalks, and grow familiar with the countless indescribable forms of the insects and flies, then I feel the presence of the Almighty, who formed us in his own image, and the breath ";

    @Test
    public void searchesAvailableDocuments() throws JSONException {
        postDocument("kafka", KAFKA_TRANSFORMATION);
        postDocument("languages", EUROPEAN_LANGUAGES);
        postDocument("werther", WERTHER);

        searchDocuments(asList("friend", "like", "me"), asList("languages", "werther"));

        getDocument("languages", EUROPEAN_LANGUAGES);
    }


    private void getDocument(String documentKey, String expectedText) throws JSONException {
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/document/" + documentKey, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("{documentKey: {key:\"" + documentKey + "\"}, text: \"" + expectedText + "\"}", getResponse.getBody(), true);
    }

    private void postDocument(String documentKey, String text) throws JSONException {
        ResponseEntity<String> postResponse = restTemplate.postForEntity("/document/" + documentKey, text, String.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("{key:\"" + documentKey + "\"}", postResponse.getBody(), false);
    }

    private void searchDocuments(List<String> tokens, List<String> matchingDocuments) throws JSONException {
        String queryString = String.join("&", tokens.stream().map(token -> "token="+ token).collect(toList()));

        ResponseEntity<String> searchResponse = restTemplate.getForEntity("/document/search?" + queryString, String.class);
        assertThat(searchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expectedJson =
                "["
                + String.join(",", matchingDocuments.stream().map(document -> "{key:\"" + document + "\"}").collect(toList()))
                + "]";
        JSONAssert.assertEquals(expectedJson, searchResponse.getBody(), false);

    }
}
