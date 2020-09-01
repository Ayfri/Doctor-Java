package fr.ayfri.doctorjava;

import fr.ayfri.doctorjava.commands.CommandManager;
import fr.ayfri.doctorjava.events.EventManager;
import fr.ayfri.doctorjava.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.TeamMember;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.ayfri.doctorjava.utils.Constants.TOKEN;

public class Main {
	public static JDABuilder jdaClient;
	public static JDA client;
	public static ArrayList<String> prefixes = new ArrayList<>(Arrays.asList("java.", "!"));
	public static ArrayList<String> admins = new ArrayList<>(List.of("406148304448126976"));
	
	public static void main(String[] args) {
		Logger.info("Launching.", "Init");
		jdaClient = JDABuilder.createDefault("").setEventManager(new AnnotatedEventManager());
		//noinspection InstantiationOfUtilityClass
		jdaClient.addEventListeners(new EventManager());
		jdaClient.setActivity(Activity.watching("Ayfri en train de le dev"));
		CommandManager.init();
		
		login(TOKEN);
	}
	
	public static void login(String token) {
		try {
			client = jdaClient.setToken(token).build();
			setDefaultAdmins();
			Logger.info("Login in the bot.", "Launching");
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}
	
	public static void setDefaultAdmins() {
		ApplicationInfo applicationInfo = client.retrieveApplicationInfo().complete();
		
		if (applicationInfo.getTeam() != null) {
			for (TeamMember teamMember : applicationInfo.getTeam().getMembers()) {
				admins.add(teamMember.getUser().getId());
			}
		} else {
			admins.add(applicationInfo.getOwner().getId());
		}
		Logger.log("Default admins have been set.", "Configuring");
	}
}
