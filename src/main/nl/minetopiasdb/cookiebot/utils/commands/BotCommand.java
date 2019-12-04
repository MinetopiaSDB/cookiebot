package nl.minetopiasdb.cookiebot.utils.commands;

import net.dv8tion.jda.api.entities.Message;

public interface BotCommand {

	public void execute(Command cmd, String[] args, Message msg);
	
}