package nl.minetopiasdb.cookiebot.data.stocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StockAPI {

	public static StockValue getValue(String symbol, String apiToken) {
		try {
			JsonObject object = readJsonFromUrl(
					new URL("https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiToken));
			if (object == null) {
				return null;
			}
			double currentPrice = object.get("c").getAsDouble();
			double open = object.get("o").getAsDouble();

			return new StockValue((int) Math.round(open), (int) Math.round(currentPrice));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static JsonObject readJsonFromUrl(URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("User-Agent", "SDB CookieBot");
		conn.setConnectTimeout(1000);
		conn.setRequestMethod("GET");
		conn.connect();

		if (conn.getResponseCode() == 401) {
			return null;
		}
		InputStream is = (InputStream) conn.getContent();
		InputStreamReader reader = new InputStreamReader(is);

		JsonElement root = new JsonParser().parse(reader);

		reader.close();
		is.close();

		return root.getAsJsonObject();
	}
}
