package nl.minetopiasdb.cookiebot.utils.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface BotCommand {

	void execute(Command cmd, SlashCommandEvent event);
	
}