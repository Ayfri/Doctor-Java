package fr.ayfri.doctorjava.entities;

public enum LogType {
	FATAL(31),
	ERROR(31),
	WARN(33),
	INFO(36),
	LOG(37),
	DEBUG(39);
	
	private final int color;
	
	LogType(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
}
