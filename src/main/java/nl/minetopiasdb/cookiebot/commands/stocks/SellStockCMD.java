package nl.minetopiasdb.cookiebot.commands.stocks;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.data.stocks.StockData;
import nl.minetopiasdb.cookiebot.data.stocks.StockUserData;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class SellStockCMD implements BotCommand {

	@Override
	public void execute(Command cmd, SlashCommandEvent event) {
		BotConfig bc = BotConfig.getInstance();
		String symbol = BotConfig.getInstance().stocks.keySet().stream()
				.filter(stockSymbol -> stockSymbol.equals(event.getOption("aandeel").getAsString()))
				.findFirst().orElse(null);

		OptionMapping amountOption = event.getOption("hoeveelheid");
		long amount = amountOption == null ? 1 : amountOption.getAsLong();
		if (amount <= 0) {
			event.reply("Je kunt geen negatief aantal aandelen kopen!")
					.setEphemeral(true)
					.queue();
			return;
		}

		if (StockUserData.getInstance().getStocks(event.getUser().getIdLong(), symbol) < amount) {
			event.reply("Jij hebt niet zoveel aandelen in **"
					+ bc.stocks.get(symbol) + "**!")
					.setEphemeral(true).queue();
			return;
		}
		long costs = amount * StockData.getInstance().getValue(symbol).getCurrentPrice();
		CookieData.getInstance().addCookies(event.getUser().getIdLong(), costs);

		StockUserData.getInstance().setStocks(event.getUser().getIdLong(), symbol,
				StockUserData.getInstance().getStocks(event.getUser().getIdLong(), symbol) - amount);
		event.reply("Jij hebt succesvol **" + amount+ "** aandelen in " +
				"**" + bc.stocks.get(symbol) + "** verkocht voor **" + BotConfig.getInstance().format(costs) + " koekjes**.")
				.queue();
	}

}
