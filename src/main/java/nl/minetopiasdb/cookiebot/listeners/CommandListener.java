package nl.minetopiasdb.cookiebot.listeners;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.commands.CommandFactory;

public class CommandListener extends ListenerAdapter {

	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		if (BotConfig.getInstance().COOKIE_CHANNEL_ID != -1L
				&& BotConfig.getInstance().COOKIE_CHANNEL_ID != event.getChannel().getIdLong()) {
			long channelId = BotConfig.getInstance().COOKIE_CHANNEL_ID;
			event.reply("Deze commando's werken alleen in <#" + channelId + ">.").setEphemeral(true).queue();
			return;
		}
		CommandFactory.getInstance().execute(event.getName(), event);
	}
}
