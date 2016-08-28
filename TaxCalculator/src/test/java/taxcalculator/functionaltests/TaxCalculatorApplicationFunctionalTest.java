package taxcalculator.functionaltests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taxcalculator.application.TaxCalculatorApplication;


public class TaxCalculatorApplicationFunctionalTest {

	private static TaxCalculatorApplication application;
	private static HttpClient httpClient;
	private HttpPost httpPost;
	
	@BeforeClass
	public static void beforeAll() {
		application = new TaxCalculatorApplication();
		application.start();
		
		httpClient = HttpClients.createDefault();
	}
	
	@AfterClass
	public static void aferAll() {
		application.stop();
	}
	
	@Before
	public void setUp() {
		httpPost = new HttpPost("http://localhost:8080/taxcalculator");
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("Accept", "application/json");
	}
	
	@Test
	public void calculatesSalesTaxForListOfItems() throws ClientProtocolException, IOException {
		String importedPerfumeJson = itemJson("imported bottle of perfume", "27.99", 2);
		String perfumeJson = itemJson("bottle of perfume", "18.99", 1);
		String pillsJson = itemJson("packet of headache pills", "9.75", 1);
		String importedChocolateJson = itemJson("box of imported chocolates", "11.25", 1);
		httpPost.setEntity(new StringEntity("[" + String.join(",", importedPerfumeJson, perfumeJson, pillsJson, importedChocolateJson)+ "]"));
		HttpResponse httpResponse = httpClient.execute(httpPost);
		String response = EntityUtils.toString(httpResponse.getEntity());
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), is(200));
		assertThat(response, sameJSONAs("{salesTax:10.90}"));
	}
	
	@Test
	public void returnsErrorMessageIfInputDataIsIncorrect() throws ClientProtocolException, IOException {
		String importedPerfumeJson = itemJson("imported bottle of perfume", "27.99", 2);
		String wrongItemJson = itemJson("bottle of perfume", "18.99", -1);
		httpPost.setEntity(new StringEntity("[" + String.join(",", importedPerfumeJson, wrongItemJson)+ "]"));
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		String response = EntityUtils.toString(httpResponse.getEntity());
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), is(400));
		assertThat(response, sameJSONAs("{message:\"Failed to create item for '-1 bottle of perfume at 18.99'\"}").allowingExtraUnexpectedFields());		
	}
	
	
	private String itemJson(String description, String price, int quantity) {
		return String.format("{\"description\": \"%s\",\"count\": %d,\"unitPrice\": %s}", description, quantity, price);
	}	
}
