package nl.minetopiasdb.cookiebot.utils;

import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.cdimascio.dotenv.Dotenv;

public class BotConfig {

	private static BotConfig instance;
	private final static DecimalFormat COOKIE_FORMAT = new DecimalFormat("#,###");

	public Map<String, String> stocks = new HashMap<>();
	public String BOT_TOKEN, MYSQL_HOST, MYSQL_DATABASE, MYSQL_USERNAME, MYSQL_PASSWORD, FINNHUB_KEY;
	public int MYSQL_PORT;
	public Long DONATOR_ROLE_ID, GUILD_ID, COOKIE_CHANNEL_ID;

	public static BotConfig getInstance() {
		if (instance == null) {
			instance = new BotConfig();
		}
		return instance;
	}

	public void initialise() {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		BOT_TOKEN = dotenv.get("DISCORD_BOT_TOKEN");

		MYSQL_HOST = dotenv.get("DB_HOST");
		MYSQL_PORT = Integer.parseInt(dotenv.get("DB_PORT"));
		MYSQL_DATABASE = dotenv.get("DB_DATABASE");
		MYSQL_USERNAME = dotenv.get("DB_USERNAME");
		MYSQL_PASSWORD = dotenv.get("DB_PASSWORD");

		GUILD_ID = Long.parseLong(dotenv.get("GUILD_ID"));
		DONATOR_ROLE_ID = Long.parseLong(dotenv.get("DONATOR_ROLE_ID"));
		COOKIE_CHANNEL_ID = Long.parseLong(dotenv.get("COOKIE_CHANNEL_ID"));
		FINNHUB_KEY = dotenv.get("FINNHUB_API_KEY");

		stocks = Arrays.stream(dotenv.get("ENABLED_STOCKS").split("(?<=[^\\\\]),")).map(stock_pair -> {
			String[] stock = stock_pair.split("(?<=[^\\\\]):");
			return new AbstractMap.SimpleEntry<>(stock[0], stock[1]);
		}).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
	}

	public String format(long cookies) {
		return COOKIE_FORMAT.format(cookies).replace(',', '.');
	}
}
