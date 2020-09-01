package fr.ayfri.doctorjava.events;

import fr.ayfri.doctorjava.commands.Command;
import fr.ayfri.doctorjava.commands.CommandManager;
import fr.ayfri.doctorjava.entities.ArgType;
import fr.ayfri.doctorjava.entities.Tag;
import fr.ayfri.doctorjava.utils.ArgUtils;
import fr.ayfri.doctorjava.utils.CommandUtils;
import fr.ayfri.doctorjava.utils.Logger;
import fr.ayfri.doctorjava.utils.Utils;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.stream.Collectors;

public class EventManager {
	public static String prefixUsed = "java.";
	
	@SubscribeEvent
	public static void onMessage(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			prefixUsed = Utils.getPrefix(event.getMessage().getContentRaw());
			if (prefixUsed == null) {
				return;
			}
			
			if (event.getMessage().getContentRaw().equals(prefixUsed)) {
				CommandManager.commands.values().stream().filter(
					command -> command.hasTag(Tag.PREFIX_COMMAND)
				).collect(Collectors.toList()).forEach(
					command -> command.execute(event)
				);
				
				return;
			}
			
			final String commandArg = ArgUtils.getArg(event, 0, ArgType.COMMAND);
			if (commandArg == null) {
				CommandManager.commands.values().stream().filter(
					command -> command.hasTag(Tag.HELP_COMMAND)
				).collect(Collectors.toList()).forEach(
					command -> command.execute(event)
				);
			}
			
			final Command command = CommandManager.getCommandByNameOrAlias(commandArg);
			Logger.log("Command '" + command + "' executed" +
			           (event.isFromGuild() ?
			            " on the guild '" + event.getGuild().getName() + "' " :
			            " on private messages ")
			           + "by '" + event.getAuthor().getAsTag() + "'.", "MessageReceivedEvent");
			
			if (CommandUtils.verifyCommand(event, command)) {
				try{
					command.execute(event);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void readyEvent(ReadyEvent event) {
		Logger.info(event.getJDA().getSelfUser().getAsTag() + " ready, seeing " + event.getJDA().getGuildById("549903828863025173").getMemberCount() +
		            " members.", "Ready Event");
	}
}
