package fr.ayfri.doctorjava.commands;

import fr.ayfri.doctorjava.entities.Category;
import fr.ayfri.doctorjava.entities.CommandInformation;
import fr.ayfri.doctorjava.entities.CommandPermissions;
import fr.ayfri.doctorjava.entities.Tag;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public abstract class Command {
	private final String[] aliases;
	private final ArrayList<Tag> tags;
	private String usage;
	private CommandPermissions permissions;
	private Category category;
	private String description;
	private String name;
	
	public final static Comparator<Command> COMPARATOR = Comparator.comparing(Command::getName);
	
	public Command() {
		name = getClass().getAnnotation(CommandInformation.class).name();
		description = getClass().getAnnotation(CommandInformation.class).description();
		category = getClass().getAnnotation(CommandInformation.class).category();
		usage = getClass().getAnnotation(CommandInformation.class).usage();
		aliases = getClass().getAnnotation(CommandInformation.class).aliases();
		tags = new ArrayList<>(Arrays.asList(getClass().getAnnotation(CommandInformation.class).tags()));
		permissions = new CommandPermissions(this);
		
		CommandManager.addCommand(this);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String toObjectString() {
		return "Command: {" +
		       "\n\tname: '" + name + '\'' +
		       ",\n\tdescription: '" + description + '\'' +
		       ",\n\tcategory: '" + category + '\'' +
		       ",\n\tusage: '" + usage + '\'' +
		       ",\n\taliases: " + Arrays.toString(aliases) +
		       ",\n\ttags: " + Arrays.toString(tags.toArray()) +
		       ",\n\tpermissions: {" +
		       "\n\t\tbotPermissions: " + Arrays.toString(permissions.getBotPermissions().toArray()) +
		       ",\n\t\tmemberPermissions: " + Arrays.toString(permissions.getMemberPermissions().toArray()) +
		       "\n\t}" +
		       "\n}";
	}
	
	public abstract void execute(MessageReceivedEvent event);
	
	public boolean hasTag(Tag tag) {
		return tags.contains(tag);
	}
	
	public String[] getAliases() {
		return aliases;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(final Category category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public CommandPermissions getPermissions() {
		return permissions;
	}
	
	public void setPermissions(final CommandPermissions permissions) {
		this.permissions = permissions;
	}
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public void setUsage(final String usage) {
		this.usage = usage;
	}
}
