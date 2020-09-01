package fr.ayfri.doctorjava.entities;

public enum Tag {
	GUILD_ONLY,
	DM_ONLY,
	GUILD_OWNER_ONLY,
	CONTRIBUTOR_ONLY,
	HELP_COMMAND,
	PREFIX_COMMAND,
	NSFW_ONLY,
	NOT_STABLE;
	
	public static String getName(Tag tag) {
		return switch (tag) {
			case GUILD_ONLY -> "Sur serveur seulement";
			case DM_ONLY -> "Messages privés seulement.";
			case GUILD_OWNER_ONLY -> "Fondateur du serveur requis.";
			case CONTRIBUTOR_ONLY -> "Contributeur requis.";
			case HELP_COMMAND -> "Exécutée quand commande non trouvée.";
			case PREFIX_COMMAND -> "Exécutée quand préfixe envoyé.";
			case NSFW_ONLY -> "Salon NSFW requis.";
			case NOT_STABLE -> "Commande non-stable.";
		};
	}
}
