package nl.minetopiasdb.cookiebot.utils.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import nl.minetopiasdb.cookiebot.Main;

public class CommandFactory {

	private static CommandFactory instance = null;
	private final HashMap<Command, BotCommand> commands = new HashMap<>();

	public static CommandFactory getInstance() {
		if (instance == null) {
			instance = new CommandFactory();
		}
		return instance;
	}

	public Command registerCommand(String commandName, String description, BotCommand executor, OptionData... options) {
		if (commands.keySet().stream().anyMatch(cmd -> cmd.getName().equalsIgnoreCase(commandName))) {
			return null;
		}
		Main.getGuild().upsertCommand(new CommandData(commandName, description).addOptions(options)).queue();
		Command command = new Command(commandName, new ArrayList<>());
		commands.put(command, executor);
		return command;
	}

	public void execute(String command, SlashCommandEvent event) {
		commands.keySet().stream()
				.filter(cmd -> cmd.getName().equals(command) || cmd.getAliases().contains(command))
				.forEach(cmd -> commands.get(cmd).execute(cmd, event));
	}
}