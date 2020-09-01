package fr.ayfri.doctorjava.commands;

import fr.ayfri.doctorjava.entities.ArgType;
import fr.ayfri.doctorjava.entities.CommandInformations;
import fr.ayfri.doctorjava.entities.Tag;
import fr.ayfri.doctorjava.utils.ArgUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandInformations(
	name = "javadoc",
	aliases = { "doc" },
	tags = { Tag.NOT_STABLE },
	description = "Permet de récupérer des informations sur une classe/interface/enum/annotation depuis la documentation des JDK.",
	usage = "javadoc <Chemin D'une Classe> [numéro du JDK]"
)
public class JavaDocCommand extends Command {
	@Override
	public void execute(final MessageReceivedEvent event) {
		Message m = event.getTextChannel().sendMessage("Recherche de la classe dans la JavaDoc...").complete();
		
		final String packageArg = ArgUtils.getArg(event, 1, ArgType.PACKAGE);
		final String versionArgString = ArgUtils.getArg(event, 2, ArgType.INT);
		int versionArg = 0;
		
		if (versionArgString != null) {
			versionArg = Integer.parseInt(versionArgString);
			if (versionArg < 7) {
				event.getTextChannel().sendMessage("Désolé le JDK " + versionArg + " utilise une architecture qui fait qu'elle n'est pas parsable.").queue();
				return;
			}
			if (versionArg > 14) {
				event.getTextChannel().sendMessage("Hey Java " + versionArg + " existe pas encore hein ;)").queue();
				return;
			}
		}
		
		if (packageArg != null) {
			if (versionArg != 0) {
				JavaDocCommandUtils.searchPackage(packageArg, versionArg, m);
			} else {
				JavaDocCommandUtils.searchVersion(packageArg, m);
			}
		} else {
			ArgUtils.argError("L'argument 'package' n'est pas valide.", this, event);
		}
	}
}
