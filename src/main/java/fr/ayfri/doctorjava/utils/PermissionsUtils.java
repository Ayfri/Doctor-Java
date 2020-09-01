package fr.ayfri.doctorjava.utils;

import fr.ayfri.doctorjava.commands.Command;
import fr.ayfri.doctorjava.entities.CommandPermissions;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PermissionsUtils {
	public static CommandPermissions getMissingPermissions(Message message, Command command) {
		CommandPermissions missingPermissions = new CommandPermissions(command);
		if (message.isFromGuild()) {
			EnumSet<Permission> memberPermissions = Objects.requireNonNull(message.getMember()).getPermissions();
			EnumSet<Permission> botPermissions = message.getGuild().getSelfMember().getPermissions();
			
			ArrayList<Permission> botMissingPermissions = getMissingPermissions(command.getPermissions().getBotPermissions(), new ArrayList<>(botPermissions));
			ArrayList<Permission> memberMissingPermissions = getMissingPermissions(command.getPermissions().getMemberPermissions(), new ArrayList<>(memberPermissions));
			
			if (botMissingPermissions.size() > 0) {
				missingPermissions.botPermissions = botMissingPermissions;
			}
			if (memberMissingPermissions.size() > 0) {
				missingPermissions.memberPermissions = memberMissingPermissions;
			}
		}
		
		return missingPermissions;
	}
	
	public static ArrayList<Permission> getMissingPermissions(List<Permission> from, List<Permission> test) {
		return from.stream().filter(permission -> !test.contains(permission)).collect(Collectors.toCollection(ArrayList::new));
	}
}
