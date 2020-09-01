package fr.ayfri.doctorjava.commands;

import fr.ayfri.doctorjava.utils.Logger;

import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {
	public static final HashMap<String, Command> commands = new HashMap<>();
	
	public static void init() {
		new TestCommand();
		new HelpCommand();
		new JavaDocCommand();
		new StopCommand();
		new BotInfoCommand();
	}
	
	public static void addCommand(Command command) {
		commands.put(command.getName(), command);
		Logger.log("Command '" + command.getName() + "' added.", "CommandManager");
	}
	
	public static Command getCommandByNameOrAlias(String name) {
		return commands.values().stream().filter(command ->
			                                         command.getName().equals(name) ||
			                                         Arrays.asList(command.getAliases()).contains(name)
		).findFirst().orElseThrow();
	}
}
