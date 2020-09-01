package fr.ayfri.doctorjava.utils;

import fr.ayfri.doctorjava.Main;

import java.util.Collection;

public class Utils {
	
	public static boolean isOwner(String id) {
		return Main.admins.contains(id);
	}
	
	public static String getPrefix(String content) {
		String prefixUsed = null;
		for (final String prefix : Main.prefixes) {
			if (content.startsWith(prefix)) {
				prefixUsed = prefix;
			}
		}
		
		return prefixUsed;
	}
	
}
