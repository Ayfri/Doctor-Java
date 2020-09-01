package fr.ayfri.doctorjava.commands;

import fr.ayfri.doctorjava.entities.CommandInformation;
import fr.ayfri.doctorjava.entities.Tag;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandInformation(
	name = "stop",
	tags = { Tag.CONTRIBUTOR_ONLY },
	aliases = { "shutdown", "exit" },
	description = "Permet d'arrêter le bot."
)
public class StopCommand extends Command {
	@Override
	public void execute(final MessageReceivedEvent event) {
		event.getTextChannel().sendMessage("Extinction des feux !").queue();
		event.getJDA().shutdown();
	}
}
