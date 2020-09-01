package fr.ayfri.doctorjava.commands;

import fr.ayfri.doctorjava.entities.ArgType;
import fr.ayfri.doctorjava.entities.Category;
import fr.ayfri.doctorjava.entities.CommandInformation;
import fr.ayfri.doctorjava.entities.Tag;
import fr.ayfri.doctorjava.utils.ArgUtils;
import fr.ayfri.doctorjava.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

@CommandInformation(
	name = "help",
	category = Category.UTILS,
	tags = { Tag.HELP_COMMAND },
	description = "Donne des informations sur une commande ou affiche toutes les commandes.",
	aliases = { "h", "aide" },
	usage = "help [commande]"
)
public class HelpCommand extends Command {
	@Override
	public void execute(final MessageReceivedEvent event) {
		final String commandArg = ArgUtils.getArg(event, 1, ArgType.COMMAND);
		final EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor("Demandé par : " + event.getAuthor().getAsTag());
		embed.setColor(new Color(239, 86, 42));
		
		if (commandArg != null) {
			final Command command = CommandManager.getCommandByNameOrAlias(commandArg);
			embed.setTitle("Informations de la commande : " + commandArg);
			embed.setDescription(command.getDescription());
			embed.addField("Catégorie : ", command.getCategory().getName(), false);
			embed.setFooter("<> = Obligatoire, [] = Optionnel");
			
			if (command.getUsage().length() > 0) {
				embed.addField("Utilisation :", command.getUsage(), false);
			}
			
			if (command.getAliases().length > 0) {
				embed.addField("Alias :", FormatUtils.formatToCode(command.getAliases()), false);
			}
			
			if (command.getPermissions().getBotPermissions().size() > 0) {
				embed.addField("Permissions du bot requises :", FormatUtils.formatPermissions(command.getPermissions().getBotPermissions()), false);
			}
			
			if (command.getPermissions().getMemberPermissions().size() > 0) {
				embed.addField("Permissions du bot requises :", FormatUtils.formatPermissions(command.getPermissions().getMemberPermissions()), false);
			}
			
			if (command.getTags().size() > 0) {
				embed.addField("Tags :", FormatUtils.formatTags(command.getTags()), false);
			}
			
		} else {
			embed.setTitle("Liste des commandes : ");
			embed.setFooter("java.help [commande] pour des informations sur une commande.");
			
			StringBuilder description = new StringBuilder();
			final ArrayList<Command> commands = new ArrayList<>(CommandManager.commands.values());
			commands.sort(Command.COMPARATOR);
			for (final Command command : commands) {
				description.append("`").append(command.getName()).append("` : ").append(command.getDescription()).append("\n");
			}
			embed.setDescription(description);
		}
		
		event.getTextChannel().sendMessage(embed.build()).queue();
	}
}
