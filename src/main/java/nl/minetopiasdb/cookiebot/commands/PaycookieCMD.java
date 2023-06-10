package nl.minetopiasdb.cookiebot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class PaycookieCMD implements BotCommand {

	@Override
	public void execute(Command cmd, SlashCommandEvent event) {
		long cookies = event.getOption("hoeveelheid").getAsLong();
		Member recipient = event.getOption("user").getAsMember();
		if (recipient.getIdLong() == event.getMember().getIdLong()) {
			event.reply("Je kunt geen koekjes aan jezelf betalen!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if (cookies <= 0) {
			event.reply("Je kunt geen negatief aantal koekjes betalen!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if (CookieData.getInstance().getCookies(event.getUser().getIdLong()) < cookies) {
			event.reply("Je hebt niet genoeg cookies om dit te doen!")
					.queue();
			return;
		}

		CookieData.getInstance().removeCookies(event.getUser().getIdLong(), cookies);
		CookieData.getInstance().addCookies(recipient.getIdLong(), cookies);

		event.reply(recipient.getAsMention()).addEmbeds(MessageHandler.getHandler().getDefaultEmbed("PayCookie")
						.addField("Cookies van", Main.getGuild().getMember(event.getUser()).getEffectiveName(), true)
						.addField("Cookies voor", recipient.getEffectiveName(), true)
						.addField("Nieuwe aantal cookies van " + recipient.getEffectiveName(),
								CookieData.getInstance().getFormattedCookies(recipient.getIdLong()) + " cookies", true)
						.addField("Hoeveelheid cookies", BotConfig.getInstance().format(cookies) + " cookies", true).build())
				.queue();
	}
}
