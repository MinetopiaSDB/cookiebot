package nl.minetopiasdb.cookiebot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.cooldowns.StealcookieCooldown;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.tasks.StealCookieTask.StealData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class StealcookieCMD implements BotCommand {

	@Override
	public void execute(Command cmd, SlashCommandEvent event) {
		if (StealcookieCooldown.getInstance().hasCooldown(event.getUser().getIdLong())) {
			event.reply("Jouw wachttijd is nog niet over, je moet nog wachten tot **"
							+ StealcookieCooldown.getInstance().getTimeLeft(event.getUser().getIdLong()) + "**!")
					.setEphemeral(true)
					.queue();
			return;
		}

		User user = event.getUser();
		Member target = event.getOption("user").getAsMember();
		if (target.getIdLong() == user.getIdLong()) {
			event.reply("Je kunt geen koekjes van jezelf stelen!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if (CookieData.getInstance().getCookies(user.getIdLong()) < 3) {
			event.reply("Je hebt hier minimaal 3 koekjes voor nodig!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if (CookieData.getInstance().getCookies(target.getIdLong()) < 3) {
			event.reply("Jouw doelwit heeft op z'n minst 3 koekjes nodig")
					.setEphemeral(true)
					.queue();
			return;
		}

		StealcookieCooldown.getInstance().addUserToCooldown(user.getIdLong());

		event.reply(target.getAsMention())
				.addEmbeds(MessageHandler.getHandler().getStealCookieProgressEmbed(user, target.getUser(), 5).build())
				.queue(t -> Main.getStealCookieTask().getMap().put(user.getIdLong(),
						new StealData(t, 5, target.getIdLong())));

	}
}
