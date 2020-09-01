package fr.ayfri.doctorjava.commands;

import fr.ayfri.doctorjava.entities.Category;
import fr.ayfri.doctorjava.entities.CommandInformation;
import fr.ayfri.doctorjava.entities.Tag;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

@CommandInformation(
	name = "test",
	description = "C'est juste une commande de tests, faites pas attention mdr",
	tags = { Tag.CONTRIBUTOR_ONLY },
	category = Category.OWNER
)
public class TestCommand extends Command {
	public TestCommand() {
		getPermissions().setMemberPermissions(new ArrayList<>(Arrays.asList(Permission.MANAGE_SERVER, Permission.MANAGE_ROLES)));
	}
	
	@Override
	public void execute(final MessageReceivedEvent event) {
		event.getTextChannel().sendMessage("Hey salut mon pote, je fonctionne :D").queue();
	}
}
