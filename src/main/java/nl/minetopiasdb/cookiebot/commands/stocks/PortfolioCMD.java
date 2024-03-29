package nl.minetopiasdb.cookiebot.commands.stocks;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nl.minetopiasdb.cookiebot.data.stocks.StockData;
import nl.minetopiasdb.cookiebot.data.stocks.StockUserData;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class PortfolioCMD implements BotCommand {

	@Override
	public void execute(Command cmd, SlashCommandEvent event) {
		String desc = "";
		long totalValue = 0;
		for (String symbol : BotConfig.getInstance().stocks.keySet()) {
			String name = BotConfig.getInstance().stocks.get(symbol);

			long amount = StockUserData.getInstance().getStocks(event.getUser().getIdLong(), symbol);
			long value = amount * StockData.getInstance().getValue(symbol).getCurrentPrice();

			desc = desc + "\n**" + name + "** (" + symbol + "): " + amount + "x (" + BotConfig.getInstance().format(value) + " koekjes)";
			
			totalValue = totalValue + value;
		}
		
		EmbedBuilder builder = MessageHandler.getHandler().getDefaultEmbed("Aandelen van " + event.getUser().getName()
				+ " (totale waarde: " + BotConfig.getInstance().format(totalValue) + " koekjes)");
		builder.setDescription(desc);
		event.replyEmbeds(builder.build()).queue();
	}

}
