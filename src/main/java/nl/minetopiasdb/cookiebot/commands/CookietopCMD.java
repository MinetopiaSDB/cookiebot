package nl.minetopiasdb.cookiebot.commands;

import java.util.HashMap;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class CookietopCMD implements BotCommand {

	@Override
	public void execute(Command cmd, String[] args, Message msg) {
		HashMap<Long, Integer> cookieTop = CookieData.getInstance().getCookieTop();

		EmbedBuilder cookieTopEmbed = MessageHandler.getHandler().getDefaultEmbed("CookieTop");
		StringBuilder embedDescription = new StringBuilder("");

		var wrapper = new Object() {
			int ordinal = 1;
		};

		cookieTop.keySet().forEach(userId -> embedDescription
				.append(wrapper.ordinal++ + ". " + getName(userId) + ": " + cookieTop.get(userId) + "\n"));

		msg.getChannel().sendMessage(cookieTopEmbed.setDescription(embedDescription.toString()).build()).queue();
	}

	public String getName(long userId) {
		try {
			return Main.getGuild().retrieveMemberById(userId).complete().getEffectiveName();
		} catch (ErrorResponseException ex) {
			return "Onbekend";
		}
	}

}
