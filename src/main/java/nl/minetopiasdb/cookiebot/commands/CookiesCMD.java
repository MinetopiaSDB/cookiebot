package nl.minetopiasdb.cookiebot.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class CookiesCMD implements BotCommand {

	@Override
	public void execute(Command cmd, SlashCommandEvent event) {
		OptionMapping userOption = event.getOption("user");
		User user = userOption == null ? event.getUser() : userOption.getAsUser();

		event.replyEmbeds(MessageHandler.getHandler().getDefaultEmbed("Cookies")
				.setDescription(user.getName() + ": " + CookieData.getInstance().getCookies(user.getIdLong()))
				.build())
				.queue();
	}
}
