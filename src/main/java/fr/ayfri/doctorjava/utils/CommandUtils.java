package fr.ayfri.doctorjava.utils;

import fr.ayfri.doctorjava.commands.Command;
import fr.ayfri.doctorjava.entities.CommandPermissions;
import fr.ayfri.doctorjava.entities.Tag;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUtils {
	
	public static boolean verifyCommand(MessageReceivedEvent event, Command command) {
		if (command.hasTag(Tag.CONTRIBUTOR_ONLY) && !Utils.isOwner(event.getAuthor().getId())) {
			ArgUtils.argError("Tag `CONTRIBUTOR_ONLY` mais l'utilisateur n'est pas un contributeur.", command, event);
			return false;
		}
		
		if (event.isFromGuild()) {
			if (event.getJDA().isUnavailable(event.getGuild().getIdLong())) {
				ArgUtils.argError("Serveur indisponible.", command, event);
				return false;
			}
			
			if (command.hasTag(Tag.DM_ONLY)) {
				ArgUtils.argError("Tag DM_ONLY sur la commande mais exécutée sur le serveur.", command, event);
				return false;
			}
			
			if (command.hasTag(Tag.GUILD_OWNER_ONLY) && !event.getGuild().getOwner().getId().equals(event.getMember().getId())) {
				ArgUtils.argError("Tag GUILD_OWNER_ONLY sur un membre lambda.", command, event);
				return false;
			}
			
			if (command.hasTag(Tag.NSFW_ONLY) && !event.getTextChannel().isNSFW()) {
				ArgUtils.argError("Tag NSFW_ONLY sur un salon non-NSFW.", command, event);
				return false;
			}
			
			CommandPermissions missingPermissions = PermissionsUtils.getMissingPermissions(event.getMessage(), command);
			if (missingPermissions.botPermissions.size() > 0) {
				ArgUtils.argError("Manque de permissions : " + FormatUtils.formatPermissions(missingPermissions.botPermissions), command, event);
				return false;
			}
			
			if (missingPermissions.memberPermissions.size() > 0) {
				ArgUtils.argError("Manque de permissions : " + FormatUtils.formatPermissions(missingPermissions.memberPermissions), command, event);
				return false;
			}
			
		} else {
			if (command.hasTag(Tag.GUILD_ONLY)) {
				ArgUtils.argError("Tag GUILD_ONLY mais exécuté en messages privés.", command, event);
				return false;
			}
		}
		
		return true;
	}
	
}
