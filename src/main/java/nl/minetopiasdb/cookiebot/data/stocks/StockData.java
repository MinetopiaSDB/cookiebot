package nl.minetopiasdb.cookiebot.data.stocks;

import java.util.HashMap;

public class StockData {

	private static StockData instance;

	private HashMap<String, StockValue> cache = new HashMap<>();

	public static StockData getInstance() {
		if (instance == null) {
			instance = new StockData();
		}
		return instance;
	}

	public void change(String symbol, int price, int openprice) {
		if (cache.containsKey(symbol)) {
			cache.get(symbol).setCurrentPrice(price);
			cache.get(symbol).setOpenPrice(openprice);
		} else {
			cache.put(symbol, new StockValue(openprice, price));
		}
	}

	public StockValue getValue(String symbol) {
		return cache.get(symbol);
	}

}
