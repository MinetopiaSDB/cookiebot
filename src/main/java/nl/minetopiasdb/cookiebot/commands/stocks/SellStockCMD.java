package nl.minetopiasdb.cookiebot.commands.stocks;

import net.dv8tion.jda.api.entities.Message;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.data.stocks.StockData;
import nl.minetopiasdb.cookiebot.data.stocks.StockUserData;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class SellStockCMD implements BotCommand {

	@Override
	public void execute(Command cmd, String[] args, Message msg) {
		if (args.length < 2) {
			msg.getChannel()
					.sendMessage(msg.getAuthor().getAsMention() + ", gebruik !verkoopaandeel <Afkorting> <Hoeveelheid>")
					.queue();
			return;
		}
		if (!BotConfig.getInstance().stocks.keySet().stream().filter(symbol -> args[0].equalsIgnoreCase(symbol))
				.findFirst().isPresent()) {
			msg.getChannel()
					.sendMessage(msg.getAuthor().getAsMention() + ", gebruik !verkoopaandeel <Afkorting> <Hoeveelheid>")
					.queue();
			return;
		}
		String symbol = BotConfig.getInstance().stocks.keySet().stream()
				.filter(stockSymbol -> args[0].equalsIgnoreCase(stockSymbol)).findFirst().get();
		int amount = -1;
		try {
			amount = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			msg.getChannel()
					.sendMessage(msg.getAuthor().getAsMention() + ", gebruik !verkoopaandeel <Afkorting> <Hoeveelheid>")
					.queue();
			return;
		}
		if (amount <= 0) {
			msg.getChannel()
					.sendMessage(msg.getAuthor().getAsMention() + ", gebruik !verkoopaandeel <Afkorting> <Hoeveelheid>")
					.queue();
			return;
		}
		if (StockUserData.getInstance().getStocks(msg.getAuthor().getIdLong(), symbol) < amount) {
			msg.getChannel().sendMessage(msg.getAuthor().getAsMention() + ", jij hebt niet zoveel aandelen in **"
					+ BotConfig.getInstance().stocks.get(symbol) + "**!").queue();
			return;
		}
		int costs = amount * StockData.getInstance().getValue(symbol).getCurrentPrice();
		CookieData.getInstance().addCookies(msg.getAuthor().getIdLong(), costs);

		StockUserData.getInstance().setStocks(msg.getAuthor().getIdLong(), symbol,
				StockUserData.getInstance().getStocks(msg.getAuthor().getIdLong(), symbol) - amount);
		msg.getChannel()
				.sendMessage(msg.getAuthor().getAsMention() + ", jij hebt succesvol **" + amount + "** aandelen in **"
						+ BotConfig.getInstance().stocks.get(symbol) + "** verkocht voor **" + costs + " koekjes**.")
				.queue();
	}

}
