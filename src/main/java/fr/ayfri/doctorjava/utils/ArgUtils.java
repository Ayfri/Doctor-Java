package fr.ayfri.doctorjava.utils;

import fr.ayfri.doctorjava.Main;
import fr.ayfri.doctorjava.commands.Command;
import fr.ayfri.doctorjava.commands.CommandManager;
import fr.ayfri.doctorjava.entities.ArgType;
import fr.ayfri.doctorjava.entities.Category;
import fr.ayfri.doctorjava.events.EventManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;


public class ArgUtils {
	public static String getArg(MessageReceivedEvent event, int number, ArgType type) {
		return getArg(event.getMessage().getContentRaw().substring(EventManager.prefixUsed.length()), number, type);
	}
	
	public static String getArg(String content, int number, ArgType type) {
		List<String> args = Arrays.asList(content.split("\\s+"));
		if (args.size() - 1 < number) {
			return null;
		}
		
		if (isValidType(args.get(number), type)) {
			return args.get(number);
		}
		
		return null;
	}
	
	public static boolean isValidType(String arg, ArgType type) {
		return switch (type) {
			case INT -> Integer.toString(Integer.parseInt(arg)).equals(arg);
			
			case DOUBLE -> Double.toString(Double.parseDouble(arg)).equals(arg);
			
			case COMMAND -> CommandManager.commands.containsKey(arg) ||
			                CommandManager.commands.values().stream().anyMatch(command -> Arrays.asList(command.getAliases()).contains(arg));
			
			case PACKAGE -> arg.matches("[a-z]+\\.([a-zA-Z]+\\.?)+");
			
			case USER_ID -> Main.client.getUserById(arg) != null;
			
			case CHANNEL_ID -> Main.client.getGuildChannelById(arg) != null;
			
			case CATEGORY -> Category.getFromName(arg) != null;
		};
	}
	
	public static void argError(String reason, Command command, MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor("Commande exécutée par : " + event.getAuthor().getAsTag());
		embed.setTitle("La commande " + command.getName() + " n'a pas pu s'exécuter.");
		embed.setDescription("Raison : **`" + reason + "`**");
		
		event.getTextChannel().sendMessage(embed.build()).queue();
	}
}
