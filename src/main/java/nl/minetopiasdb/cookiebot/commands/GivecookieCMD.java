package nl.minetopiasdb.cookiebot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.cooldowns.GivecookieCooldown;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class GivecookieCMD implements BotCommand {

	@Override
	public void execute(Command cmd, SlashCommandEvent event) {
		User user = event.getUser();

		if (!isDonator(user)) {
			event.reply("Je moet de Donator rol hebben om ``/givecookie`` te kunnen gebruiken!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if (GivecookieCooldown.getInstance().hasCooldown(user.getIdLong())) {
			event.reply("Jouw wachttijd is nog niet over, je moet nog wachten tot **"
							+ GivecookieCooldown.getInstance().getTimeLeft(user.getIdLong()) + "**!")
					.setEphemeral(true)
					.queue();
			return;
		}
		Member receiver = event.getOption("user").getAsMember();

		GivecookieCooldown.getInstance().addUserToCooldown(user.getIdLong());

		CookieData.getInstance().addCookies(receiver.getIdLong(), 5);

		event.reply(receiver.getAsMention()).addEmbeds(
						MessageHandler.getHandler().getGiveCookieEmbed(Main.getGuild().getMember(user), receiver).build())
				.queue();
	}

	public boolean isDonator(User user) {
		return Main.getGuild().getMember(user).getRoles().stream()
				.filter(role -> role.getIdLong() == BotConfig.getInstance().DONATOR_ROLE_ID).count() == 1;
	}
}
