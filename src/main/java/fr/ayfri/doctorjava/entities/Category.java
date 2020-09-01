package fr.ayfri.doctorjava.entities;

import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public enum Category {
	OWNER("Contribution", new Permission[]{}),
	ADMIN("Administration", new Permission[]{ Permission.ADMINISTRATOR }),
	MODERATION("Modération", new Permission[]{ Permission.KICK_MEMBERS }),
	UTILS("Utilitaires"),
	INFO("Informations");
	
	private final Permission[] permissions;
	private final String name;
	Category(String name, final Permission[] permissions) {
		this.permissions = permissions;
		this.name = name;
	}
	
	Category(String name) {
		this(name, new Permission[]{});
	}
	
	public String getName() {
		return name;
	}
	
	public static Category getFromName(String name) {
		return Arrays.stream(Category.values()).filter(category -> category.getName().equals(name)).findFirst().orElse(OWNER);
	}
	
	public Permission[] getPermissions() {
		return permissions;
	}
}
