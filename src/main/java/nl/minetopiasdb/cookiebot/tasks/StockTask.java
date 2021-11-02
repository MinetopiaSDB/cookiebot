package nl.minetopiasdb.cookiebot.tasks;

import java.util.TimerTask;

import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.stocks.FinnhubAPI;
import nl.minetopiasdb.cookiebot.data.stocks.StockData;
import nl.minetopiasdb.cookiebot.data.stocks.StockPrice;
import nl.minetopiasdb.cookiebot.utils.BotConfig;

public class StockTask extends TimerTask {

	@Override
	public void run() {
		for (String symbol : BotConfig.getInstance().stocks.keySet()) {
			StockPrice value = Main.getFinnhubAPI().getValue(symbol);
			if (value == null) {
				return;
			}
			int openPrice = value.getOpenPrice();
			int currentPrice = value.getCurrentPrice();

			StockData.getInstance().change(symbol, currentPrice, openPrice);
		}
	}
}
