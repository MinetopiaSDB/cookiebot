package nl.minetopiasdb.cookiebot.commands.stocks;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import nl.minetopiasdb.cookiebot.data.stocks.StockData;
import nl.minetopiasdb.cookiebot.data.stocks.StockUserData;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class PortfolioCMD implements BotCommand {

	@Override
	public void execute(Command cmd, String[] args, Message msg) {
		String desc = "";
		int totalValue = 0;
		for (String symbol : BotConfig.getInstance().stocks.keySet()) {
			String name = BotConfig.getInstance().stocks.get(symbol);

			int value = StockUserData.getInstance().getStocks(msg.getAuthor().getIdLong(), symbol) * StockData.getInstance().getValue(symbol).getCurrentPrice();
			desc = desc + "\n**" + name + "** (" + symbol + "): " + StockUserData.getInstance().getStocks(msg.getAuthor().getIdLong(), symbol) + "x (" + value + " koekjes)";
			
			totalValue = totalValue + value;
		}
		
		EmbedBuilder builder = MessageHandler.getHandler().getDefaultEmbed("Aandelen van " + msg.getAuthor().getName() + " (totale waarde: " + totalValue + " koekjes)");
		builder.setDescription(desc);
		msg.getChannel().sendMessage(builder.build()).queue();
	}

}
