package nl.minetopiasdb.cookiebot.commands;

import java.util.AbstractMap;
import java.util.function.Consumer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.cooldowns.EatcookieCooldown;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class EatcookieCMD implements BotCommand {

	@Override
	public void execute(Command cmd, String[] args, Message msg) {
		User user = msg.getAuthor();
		if (EatcookieCooldown.getInstance().hasCooldown(user.getIdLong())) {
			msg.getChannel()
					.sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
							.setDescription("Jouw wachttijd is nog niet over, je moet nog wachten tot **"
									+ EatcookieCooldown.getInstance().getTimeLeft(user.getIdLong()) + "**!")
							.build())
					.queue();
			return;
		}
		if (CookieData.getInstance().getCookies(user.getIdLong()) <= 0) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Je moet minimaal 1 koekje hebben om op te eten!").build()).queue();
			return;
		}

		EatcookieCooldown.getInstance().addUserToCooldown(user.getIdLong());

		CookieData.getInstance().removeCookies(user.getIdLong(), 1);

		msg.getChannel().sendMessage(MessageHandler.getHandler()
				.getEatCookieProgressEmbed(Main.getGuild().getMemberById(user.getIdLong()).getUser(), 5).build())
				.queue(new Consumer<Message>() {
					@Override
					public void accept(Message t) {
						Main.getEatCookieTask().getMap().put(user.getIdLong(), new AbstractMap.SimpleEntry<>(t, 5));
					}
				});
	}
}
