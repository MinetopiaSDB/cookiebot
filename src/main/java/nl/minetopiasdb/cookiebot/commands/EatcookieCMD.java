package nl.minetopiasdb.cookiebot.commands;

import java.util.AbstractMap;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.cooldowns.EatcookieCooldown;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class EatcookieCMD implements BotCommand {

	@Override
	public void execute(Command cmd, SlashCommandEvent event) {
		User user = event.getUser();
		if (EatcookieCooldown.getInstance().hasCooldown(user.getIdLong())) {
			event.reply("Jouw wachttijd is nog niet over, je moet nog wachten tot **" +
					EatcookieCooldown.getInstance().getTimeLeft(user.getIdLong()) + "**!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if (CookieData.getInstance().getCookies(user.getIdLong()) <= 0) {
			event.reply("Je moet minimaal 1 koekje hebben om op te eten!")
					.setEphemeral(true)
					.queue();
			return;
		}

		EatcookieCooldown.getInstance().addUserToCooldown(user.getIdLong());

		CookieData.getInstance().removeCookies(user.getIdLong(), 1);

		event.replyEmbeds(MessageHandler.getHandler()
				.getEatCookieProgressEmbed(
						Main.getGuild().retrieveMemberById(user.getIdLong(), false).complete().getUser(), 5)
				.build())
				.queue(t -> Main.getEatCookieTask().getMap().put(user.getIdLong(),
						new AbstractMap.SimpleEntry<>(t, 5)));

	}
}
