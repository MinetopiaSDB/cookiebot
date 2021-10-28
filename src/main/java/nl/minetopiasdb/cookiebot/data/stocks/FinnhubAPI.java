package nl.minetopiasdb.cookiebot.data.stocks;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FinnhubAPI {

	private HttpClient httpClient;
	private String apiToken;

	public FinnhubAPI(String apiToken) {
		this.apiToken = apiToken;

		httpClient = HttpClient.newHttpClient();
	}

	public StockPrice getValue(String symbol) {
		try {
			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", symbol, apiToken)))
					.GET()
					.build();
			HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

			JsonObject object = new JsonParser().parse(response.body()).getAsJsonObject();

			double currentPrice = object.get("c").getAsDouble();
			double open = object.get("o").getAsDouble();

			return new StockPrice((int) Math.round(open), (int) Math.round(currentPrice));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
