package fr.ayfri.doctorjava.commands;

import com.sun.management.OperatingSystemMXBean;
import fr.ayfri.doctorjava.entities.Category;
import fr.ayfri.doctorjava.entities.CommandInformation;
import fr.ayfri.doctorjava.utils.Constants;
import fr.ayfri.doctorjava.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@CommandInformation(name = "botinfo",
	description = "Donne des informations sur le bot.",
	aliases = { "bot", "binfo" },
	category = Category.INFO
)
public class BotInfoCommand extends Command {
	@Override
	public void execute(final MessageReceivedEvent event) {
		event.getTextChannel().sendMessage(createEmbed(event.getJDA())).queue();
	}
	
	public MessageEmbed createEmbed(final JDA jda) {
		final long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
		final long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
		final OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
		final EmbedBuilder embed = new EmbedBuilder();
		final String ping = String.valueOf(jda.getRestPing().complete());
		final String startedSince = FormatUtils.secondsToTime(Constants.START.until(OffsetDateTime.now(), ChronoUnit.SECONDS));
		
		embed.setTitle("Informations sur " + jda.getSelfUser().getAsTag());
		embed.setDescription("Je suis " + jda.getSelfUser().getAsMention() + ", un docteur officiel en Java ! \nJe suis là pour aider nos <@&612608705698988042> à faire leurs expériences, je suis l'outil pratique que tout le monde aimerait avoir. \n\nJe suis développé par notre bon vieux <@386893236498857985>.");
		embed.addField("Mémoire :", usedMemory + "MB / " + totalMemory + "MB", true);
		embed.addField("Ping :", ping, true);
		embed.addField("En ligne depuis :", startedSince, true);
		embed.addField("Utilisation du CPU :", String.valueOf(operatingSystemMXBean.getProcessCpuLoad()).substring(0, 4) + "%", true);
		embed.addField("Technologies utilisées :",
		               "JDK : [" + System.getProperty("java.version") + "](https://jdk.java.net/14/)" +
		               "\nLibrairie : [JDA " + JDAInfo.VERSION + "](" + JDAInfo.GITHUB + ")",
		               true);
		
		return embed.build();
	}
}
