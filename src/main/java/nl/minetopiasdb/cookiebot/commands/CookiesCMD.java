package nl.minetopiasdb.cookiebot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class CookiesCMD implements BotCommand {

	@Override
	public void execute(Command cmd, String[] args, Message msg) {
		User user = msg.getAuthor();
		
		if (msg.getMentionedMembers().size() > 0) {
			user = msg.getMentionedMembers().get(0).getUser();
		}
		
		msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Cookies")
				.setDescription(user.getName() + ": " + CookieData.getInstance().getCookies(user.getIdLong())).build())
				.queue();
	}
}
