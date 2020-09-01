package fr.ayfri.doctorjava.utils;

public class EmbedUtils {
	public static String cutForField(String text) {
		return cutIfTooLong(text, 1024);
	}
	
	public static String cutIfTooLong(String text, int length) {
		if (text.length() > length) {
			text = text.substring(0, length - 4) + "...";
		}
		
		return text;
	}
	
	public static String cutForDescription(String text) {
		return cutIfTooLong(text, 2048);
	}
}
