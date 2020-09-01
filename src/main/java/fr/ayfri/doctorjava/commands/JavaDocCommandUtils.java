package fr.ayfri.doctorjava.commands;

import fr.ayfri.doctorjava.entities.ArgType;
import fr.ayfri.doctorjava.utils.ArgUtils;
import fr.ayfri.doctorjava.utils.EmbedUtils;
import fr.ayfri.doctorjava.utils.FormatUtils;
import fr.ayfri.doctorjava.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class JavaDocCommandUtils {
	
	private static int version;
	private static String packageLink;
	
	public static void searchPackage(String name, int version, Message message) {
		try {
			packageLink = name;
			JavaDocCommandUtils.version = version;
			parse(message, JavaDocCommandUtils.getLinkFromPackage(name, version), packageLink, version);
		} catch (Exception e) {
			if (e.getMessage() == null || !Objects.requireNonNull(e.getMessage()).contains("HTTP error fetching URL. Status=404")) {
				e.printStackTrace();
			}
			
			message.editMessage("La classe n'a pas été trouvé.").queue();
		}
	}
	
	public static void searchVersion(String name, Message message) {
		String url;
		int version = 14;
		for (; version > 6; version--) {
			try {
				url = getLinkFromPackage(name, version);
				message.editMessage("Recherche dans le JDK " + version + "...").queue();
				System.out.println("Searching in : " + url);
				
				final InputStream inputStream = new URL(url).openStream();
				if (inputStream != null) {
					inputStream.close();
					break;
				}
			} catch (IOException e) {
				if (!e.getMessage().contains("HTTP error fetching URL. Status=404")) {
					e.printStackTrace();
				}
			}
		}
		
		searchPackage(name, version, message);
	}
	
	public static String getLinkFromPackage(String packageName, int version) {
		final String[] jdkNextBasePackage = new String[]{ "io", "lang", "math", "net", "nio", "security", "text", "time", "util" };
		final int newJavaDocJDKVersion = 10;
		final String link = version > newJavaDocJDKVersion ? "https://docs.oracle.com/en/java/javase/" : "https://docs.oracle.com/javase/";
		String[] packages = packageName.split("\\.");
		StringBuilder url = new StringBuilder(link + version + "/docs/api/");
		int index = 2;
		
		url.append(version > newJavaDocJDKVersion ? "java" : packages[0]).append(version > newJavaDocJDKVersion ? "." : "/").append(packages[1]);
		if (version > newJavaDocJDKVersion) {
			// For 'javax' classes
			if ("javax".equals(packages[0])) {
				url.append("javax/").append(packages[1]);
			}
			
			// For 'jdk' classes
			if ("com".equals(packages[0])) {
				url = new StringBuilder(link + version + "/docs/api/jdk." + packages[1]);
				index = 0;
			}
			
			if (Arrays.asList(jdkNextBasePackage).contains(packages[1])) {
				url = new StringBuilder(link + version + "/docs/api/java.base/java/" + packages[1]);
			}
		}
		
		if (index > packages.length - 1) {
			index = 0;
		}
		
		do {
			url.append("/").append(packages[index]);
			index++;
		} while (index < packages.length);
		return url.append(".html").toString();
	}
	
	public static void parse(Message message, String link, String packageLink, int version) throws IOException {
		final EmbedBuilder embed = new EmbedBuilder();
		final Document site = Jsoup.connect(link).get();
		final String typeText = site.getElementsByClass("header").select(".title").text();
		final Elements blockList = version > 12 ? site.select(".description") : site.select(".description > .blockList");
		final Elements headerElements = blockList.select("dl");
		final Elements inheritance = site.select(".contentContainer .inheritance").select("li > a[title*=class]");
		final String deprecated = blockList.select(".deprecationBlock .deprecationComment").text();
		final String description = blockList.select(".block").first().html();
		String type = typeText.split(" ")[0].toLowerCase();
		Logger.log("Package " + packageLink + " found in JDK " + version + " !", "JavaDocCommand");
		
		// todo GENERICS
		
		for (final Element element : headerElements) {
			final Elements aElements = element.select("dd > code > a");
			final String join = FormatUtils.join(element.parent().select("pre").eachText(), " ");
			final String headerElement = element.select("dt").text();
			
			
			/**
			 * alors ça marche pas parce que maintenant le <code>java.</code> despawn du href, ouais c'est chiant
			 *
			 * t'as essayé de corriger et ça marche pas, tu continueras mais t'es sur la bonne voix frère
			 */
			final String packageList = EmbedUtils.cutForField(transformElementsToPackageList(aElements));
			
			if (!join.toLowerCase().contains(typeText.toLowerCase())) {
				continue;
			}
			
			if (headerElement.contains("Known Subclasses")) {
				embed.addField("Classes filles :", packageList, true);
			}
			
			if (headerElement.contains("Known Subinterfaces")) {
				embed.addField("Interfaces filles :", packageList, true);
			}
			
			if (headerElement.contains("All Superinterfaces")) {
				embed.addField("Interfaces mères :", EmbedUtils.cutForField(formatToInheritance(aElements)), true);
			}
			
			if (headerElement.contains("Implemented Interfaces")) {
				embed.addField("Interfaces implémentées :", packageList, true);
			}
			
			if (headerElement.contains("Implementing Classes")) {
				embed.addField("Classes implémentant cette interface :", packageList, true);
			}
			
			if (element.select("dt > span").text().contains("Since:")) {
				for (final Element ddElement : element.select("dd")) {
					if (ddElement.text().matches("[0-9]\\.[0-9]")) {
						embed.addField("Existe depuis le JDK :", ddElement.text(), true);
					}
				}
			}
			if (element.select("dt > span").text().contains("See Also:")) {
				embed.addField("Voir aussi :", transformElementsToPackageList(element.select("dd > a")), true);
			}
		}
		
		if (!deprecated.isEmpty()) {
			embed.addField("Dépréciée :", deprecated, true);
		}
		
		if (inheritance.size() != 0) {
			embed.addField("Héritage :", formatToInheritance(inheritance), false);
		}
		
		if ("class".equals(type)) {
			type = "la classe";
		} else {
			type = "l'" + type;
		}
		
		embed.setAuthor("Informations sur " + type +
		                (
			                blockList.select("li > pre").text().contains("abstract") ?
			                " abstraite " :
			                " "
		                ) + ": " + packageLink, link);
		embed.setTitle("Informations du JDK " + version + ".");
		embed.setDescription(EmbedUtils.cutForDescription(parseTextContainingCode(description)));
		embed.setFooter("TIP : Vous pouvez cliquer sur le titre pour aller sur la documentation !");
		
		message.delete().queue();
		message.getTextChannel().sendMessage(embed.build()).queue();
	}
	
	public static String formatToInheritance(Elements elements) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < elements.size(); i++) {
			String element = elements.get(i).text();
			if (!ArgUtils.isValidType(element, ArgType.PACKAGE)) {
				element = getPackageFromAElement(elements.get(i));
			}
			
			final String text = getLinkFromPackage(element);
			
			if (i > 0) {
				result.append("`").append("  ".repeat(i)).append("↳").append("`");
			}
			result.append(text).append("\n");
		}
		
		result.append("`").append("  ".repeat(result.toString().split("\n").length)).append("↳").append("`").append(packageLink);
		return result.toString();
	}
	
	public static String getPackageFromAElement(Element a) {
		String href = a.attr("href");
		if (href.endsWith(".html")) {
			href = href.substring(0, href.length() - 5);
		}
		
		String result = getPackageFromHref(href);
		if (Arrays.stream(new String[]{ "jdk", "java", "com" }).noneMatch(result::startsWith)) {
			final String[] split = a.attr("title").split("[ .]");
			result = split[split.length - 2];
		}
		
		return result;
	}
	
	public static String getPackageFromHref(String href) {
		final ArrayList<String> packages = new ArrayList<>(Arrays.asList(href.split("/")));
		packages.removeAll(Collections.singleton(".."));
		
		return FormatUtils.join(packages, ".");
	}
	
	public static String textToLinkedText(String text, String link) {
		return "[" + text + "]" + "(" + link + ")";
	}
	
	public static String getLinkFromPackage(String packageName) {
		return textToLinkedText(packageName, getLinkFromPackage(packageName, version));
	}
	
	public static String transformElementsToPackageList(Elements elements) {
		return elements.stream().map(element -> getLinkFromPackage(getPackageFromAElement(element)) + " ").collect(Collectors.joining());
	}
	
	public static String parseTextContainingCode(String text) {
		text = text.replaceAll("</?code>", "`");
		text = text.replaceAll("<pre>((.|\n)+)</pre>", "```java\n$1```");
		text = text.replaceAll("<a ([a-z]+=\".+\")+>(.+)</a>", "$2");
		text = text.replaceAll("</?(i|em)>", "_");
		text = text.replaceAll("_[^ ]", "_ ");
		text = text.replaceAll("<h[0-6]>(.+)</h[0-6]>", "> **$1**\n");
		text = text.replaceAll("</?ul>", "");
		text = text.replaceAll("<li>(.+)</li>", " • $1");
		text = text.replaceAll("</?strong>", "**");
		text = text.replaceAll("<p>(.+)</p>", "$1\n");
//		text = text.replaceAll("<a href=\"(.+)\">(.+)</a>", textToLinkedText("$2", "$1")); problematic with methods
		
		return text;
	}
}
