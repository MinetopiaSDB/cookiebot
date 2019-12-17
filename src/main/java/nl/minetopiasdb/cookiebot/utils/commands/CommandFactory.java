package nl.minetopiasdb.cookiebot.utils.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Message;

public class CommandFactory {

	private static CommandFactory instance = null;
	private HashMap<Command, BotCommand> commands = new HashMap<Command, BotCommand>();

	public static CommandFactory getInstance() {
		if (instance == null) {
			instance = new CommandFactory();
		}
		return instance;
	}

	public Command registerCommand(String commandName, BotCommand executor) {
		if (!commands.keySet().stream().filter(cmd -> cmd.getName().equalsIgnoreCase(commandName))
				.collect(Collectors.toList()).isEmpty()) {
			return null;
		}
		Command command = new Command(commandName, new ArrayList<>());
		commands.put(command, executor);
		return command;
	}

	public void execute(String fullProvided, Message msg) {
		String[] args = Arrays.copyOfRange(fullProvided.split(" "), 1, fullProvided.split(" ").length);
		String label = fullProvided.split(" ")[0].toLowerCase();

		commands.keySet().stream()
				.filter(cmd -> cmd.getName().equalsIgnoreCase(label) || cmd.getAliases().contains(label))
				.forEach(cmd -> commands.get(cmd).execute(cmd, args, msg));
	}
}