package nl.minetopiasdb.cookiebot.tasks;

import java.util.TimerTask;

import nl.minetopiasdb.cookiebot.data.stocks.StockAPI;
import nl.minetopiasdb.cookiebot.data.stocks.StockData;
import nl.minetopiasdb.cookiebot.data.stocks.StockValue;
import nl.minetopiasdb.cookiebot.utils.BotConfig;

public class StockTask extends TimerTask {

	@Override
	public void run() {
		for (String symbol : BotConfig.getInstance().stocks.keySet()) {
			StockValue value = StockAPI.getValue(symbol, BotConfig.getInstance().FINNHUB_KEY);
			if (value == null) {
				return;
			}
			int openPrice = value.getOpenPrice();
			int currentPrice = value.getCurrentPrice();

			StockData.getInstance().change(symbol, currentPrice, openPrice);
		}
	}
}
