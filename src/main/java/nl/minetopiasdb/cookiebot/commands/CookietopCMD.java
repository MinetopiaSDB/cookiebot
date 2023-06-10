package nl.minetopiasdb.cookiebot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class CookietopCMD implements BotCommand {

	@Override
	public void execute(Command cmd, SlashCommandEvent event) {
		event.deferReply().queue();

		Map<Long, Integer> cookieTop = CookieData.getInstance().getCookieTop();

		EmbedBuilder cookieTopEmbed = MessageHandler.getHandler().getDefaultEmbed("CookieTop");
		StringBuilder embedDescription = new StringBuilder();

		for (int i = 1; i <= 10; i++) {
			Map.Entry<Long, Integer> entry = new ArrayList<>(cookieTop.entrySet()).get(i-1);
			embedDescription.append(i + ". " + getName(entry.getKey()) + ": " + BotConfig.getInstance().format(entry.getValue()) + "\n");
		}

		event.getHook().sendMessageEmbeds(cookieTopEmbed.setDescription(embedDescription.toString()).build()).queue();
	}

	public String getName(long userId) {
		try {
			return Main.getGuild().retrieveMemberById(userId, false).complete().getEffectiveName();
		} catch (ErrorResponseException ex) {
			return "Onbekend";
		}
	}

}
