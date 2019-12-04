package nl.minetopiasdb.cookiebot.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.commands.CommandFactory;

public class CommandListener extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (BotConfig.getInstance().COOKIECHANNEL_ID == -1l
				|| BotConfig.getInstance().COOKIECHANNEL_ID == event.getChannel().getIdLong()) {
			CommandFactory.getInstance().execute(event.getMessage().getContentRaw(), event.getMessage());
		}
	}
}
