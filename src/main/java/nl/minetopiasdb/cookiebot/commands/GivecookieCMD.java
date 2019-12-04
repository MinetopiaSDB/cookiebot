package nl.minetopiasdb.cookiebot.commands;

import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.cooldowns.GivecookieCooldown;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class GivecookieCMD implements BotCommand {

	@Override
	public void execute(Command cmd, String[] args, Message msg) {
		User user = msg.getAuthor();

		if (!isDonator(user)) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Je moet de Donator rol hebben om ``!givecookie`` te kunnen gebruiken!").build())
					.queue();
			return;
		}
		if (GivecookieCooldown.getInstance().hasCooldown(user.getIdLong())) {
			msg.getChannel()
					.sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
							.setDescription("Jouw wachttijd is nog niet over, je moet nog wachten tot **"
									+ GivecookieCooldown.getInstance().getTimeLeft(user.getIdLong()) + "**!")
							.build())
					.queue();
			return;
		}
		if (msg.getMentionedMembers().size() != 1) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Fout, gebruik: **!givecookie <@User>!**").build()).queue();
			return;
		}

		GivecookieCooldown.getInstance().addUserToCooldown(user.getIdLong());
		Member receiver = msg.getMentionedMembers().get(0);
		CookieData.getInstance().addCookies(receiver.getIdLong(), 5);

		msg.getChannel().sendMessage(
				MessageHandler.getHandler().getGiveCookieEmbed(Main.getGuild().getMember(user), receiver).build())
				.queue();
	}

	public boolean isDonator(User user) {
		return Main.getGuild().getMember(user).getRoles().stream()
				.filter(role -> role.getIdLong() == BotConfig.getInstance().DONATOR_ROLE_ID).collect(Collectors.toList()).size() == 1;
	}
}
