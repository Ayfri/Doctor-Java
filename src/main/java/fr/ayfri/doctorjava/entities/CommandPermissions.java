package fr.ayfri.doctorjava.entities;

import fr.ayfri.doctorjava.commands.Command;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandPermissions {
	public final Command command;
	public ArrayList<Permission> botPermissions;
	public ArrayList<Permission> memberPermissions;
	
	public CommandPermissions(final Command command) {
		this(new ArrayList<>(), new ArrayList<>(), command);
	}
	
	public CommandPermissions(final ArrayList<Permission> botPermissions, final ArrayList<Permission> memberPermissions, final Command command) {
		this.botPermissions = botPermissions;
		this.memberPermissions = memberPermissions;
		this.command = command;
	}
	
	public ArrayList<Permission> getBotPermissions() {
		return botPermissions;
	}
	
	public void setBotPermissions(Permission[] botPermissions) {
		this.botPermissions = new ArrayList<>(Arrays.asList(botPermissions));
	}
	
	public void setBotPermissions(final ArrayList<Permission> botPermissions) {
		this.botPermissions = botPermissions;
	}
	
	public ArrayList<Permission> getMemberPermissions() {
		return memberPermissions;
	}
	
	public void setMemberPermissions(Permission[] botPermissions) {
		this.memberPermissions = new ArrayList<>(Arrays.asList(botPermissions));
	}
	
	public void setMemberPermissions(final ArrayList<Permission> memberPermissions) {
		this.memberPermissions = memberPermissions;
	}
}
