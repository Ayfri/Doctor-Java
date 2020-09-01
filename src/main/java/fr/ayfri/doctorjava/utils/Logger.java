package fr.ayfri.doctorjava.utils;

import fr.ayfri.doctorjava.entities.LogType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public static void log(String message, String title) {
		process(message, LogType.LOG, title);
	}
	
	private static void process(String message, LogType type, String title) {
		SimpleDateFormat format = new SimpleDateFormat("[dd/MM/yy hh:mm:ss.SSS]");
		String log =
			(char) 27 + "[" + type.getColor() + "m" +
			format.format(new Date()) +
			"[" + type.toString().toUpperCase() + "]"
			+ "[" + title + "] "
			+ message;
		
		System.out.println(log);
	}
	
	public static void warn(String message, String title) {
		process(message, LogType.WARN, title);
	}
	
	public static void error(String message, String title) {
		process(message, LogType.ERROR, title);
	}
	
	public static void fatal(String message, String title) {
		process(message, LogType.FATAL, title);
	}
	
	public static void info(String message, String title) {
		process(message, LogType.INFO, title);
	}
	
	public static void debug(Object object) {
		debug(object.toString(), "test");
	}
	
	public static void debug(String message, String title) {
		process(message, LogType.DEBUG, title);
	}
}
