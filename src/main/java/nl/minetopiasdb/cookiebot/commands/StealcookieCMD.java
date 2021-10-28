package nl.minetopiasdb.cookiebot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.cooldowns.StealcookieCooldown;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.tasks.StealCookieTask.StealData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class StealcookieCMD{/*implements BotCommand {

	@Override*/
	public void execute(Command cmd, String[] args, Message msg) {
		if (StealcookieCooldown.getInstance().hasCooldown(msg.getAuthor().getIdLong())) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Jouw wachttijd is nog niet over, je moet nog wachten tot **"
							+ StealcookieCooldown.getInstance().getTimeLeft(msg.getAuthor().getIdLong()) + "**!")
					.build()).queue();
			return;
		}

		if (msg.getMentionedMembers().size() != 1) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Fout, gebruik: **!steelcookie <@User>!**").build()).queue();
			return;
		}

		User user = msg.getAuthor();
		Member target = msg.getMentionedMembers().get(0);
		if (target.getIdLong() == user.getIdLong()) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Je kunt geen koekjes van jezelf stelen!").build()).queue();
			return;
		}
		if (CookieData.getInstance().getCookies(user.getIdLong()) < 3) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Je hebt hier minimaal 3 koekjes voor nodig!").build()).queue();
			return;
		}
		if (CookieData.getInstance().getCookies(target.getIdLong()) < 3) {
			msg.getChannel()
					.sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
							.setDescription("Je doelwit heeft op z'n minst 3 cookies nodig om van te stelen").build())
					.queue();
			return;
		}

		StealcookieCooldown.getInstance().addUserToCooldown(user.getIdLong());

		msg.getChannel()
				.sendMessage(MessageHandler.getHandler().getStealCookieProgressEmbed(user, target.getUser(), 5).build())
				.queue(t -> Main.getStealCookieTask().getMap().put(user.getIdLong(),
						new StealData(t, 5, target.getIdLong())));

	}
}
