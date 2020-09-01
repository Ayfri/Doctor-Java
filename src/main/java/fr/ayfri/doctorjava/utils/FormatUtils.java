package fr.ayfri.doctorjava.utils;

import fr.ayfri.doctorjava.entities.Tag;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.Collection;

public class FormatUtils {
	public static String formatToCode(Object[] array) {
		StringBuilder result = new StringBuilder("`");
		for (final Object value : array) {
			result.append(value.toString()).append("`, `");
		}
		return result.delete(result.length() - 3, result.length()).toString();
	}
	
	public static String formatToCode(Object[] array, String join) {
		StringBuilder result = new StringBuilder("`");
		for (final Object value : array) {
			result.append(value.toString());
			result.append("`");
			result.append(join);
			result.append("`");
		}
		return result.delete(result.length() + join.length(), result.length()).toString();
	}
	
	public static String formatPermissions(ArrayList<Permission> permissions) {
		StringBuilder result = new StringBuilder("[`");
		for (final Permission value : permissions) {
			result.append(value.getName()).append("`, `");
		}
		return result.delete(result.length() - 3, result.length()).append("]").toString();
	}
	
	public static String formatTags(ArrayList<Tag> tags) {
		StringBuilder result = new StringBuilder();
		for (final Tag tag : tags) {
			result.append("`").append(tag.toString()).append("` : ").append(Tag.getName(tag)).append("\n");
		}
		return result.toString();
	}
	
	public static String secondsToTime(long timeSeconds) {
		final StringBuilder builder = new StringBuilder();
		final int years = (int) (timeSeconds / (60 * 60 * 24 * 365));
		final int weeks = (int) (timeSeconds / (60 * 60 * 24 * 7));
		final int days = (int) (timeSeconds / (60 * 60 * 24));
		final int hours = (int) (timeSeconds / (60 * 60));
		final int minutes = (int) (timeSeconds / (60));
		
		if (years > 0) {
			builder.append("**").append(years).append("** ").append(pluralise(years, "an", "ans")).append(" ");
			timeSeconds = timeSeconds % (60 * 60 * 24 * 365);
		}
		
		if (weeks > 0) {
			builder.append("**").append(weeks).append("** ").append(pluralise(weeks, "semaine", "semaines")).append(" ");
			timeSeconds = timeSeconds % (60 * 60 * 24 * 7);
		}
		
		if (days > 0) {
			builder.append("**").append(days).append("** ").append(pluralise(days, "jour", "jours")).append(" ");
			timeSeconds = timeSeconds % (60 * 60 * 24);
		}
		
		builder.append("**").append(hours).append("** ").append(pluralise(hours, "heure", "heures")).append(" ");
		timeSeconds = timeSeconds % (60 * 60);
		
		builder.append("**").append(minutes).append("** ").append(pluralise(minutes, "minute", "minutes")).append(" ");
		timeSeconds = timeSeconds % (60);
		
		builder.append("**").append(timeSeconds).append("** ").append(pluralise(timeSeconds, "seconde", "secondes"));
		
		return builder.toString();
	}
	
	public static String pluralise(long x, String singular, String plural) {
		return x == 1 ? singular : plural;
	}
	
	public static String join(Collection<String> collection, String join) {
		final StringBuilder result = new StringBuilder();
		for (final String element : collection) {
			result.append(element).append(join);
		}
		result.delete(result.length() - join.length(), result.length());
		
		return result.toString();
	}
}
