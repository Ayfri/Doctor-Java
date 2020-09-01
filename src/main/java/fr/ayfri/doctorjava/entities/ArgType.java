package fr.ayfri.doctorjava.entities;

import fr.ayfri.doctorjava.utils.ArgUtils;

public enum ArgType {
	INT,
	DOUBLE,
	COMMAND,
	PACKAGE,
	USER_ID,
	CHANNEL_ID,
	CATEGORY;
	
	public boolean verify(String text) {
		return ArgUtils.isValidType(text, this);
	}
}
