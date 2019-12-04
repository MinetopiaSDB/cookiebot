package nl.minetopiasdb.cookiebot.utils;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.CookieData;

public class MessageHandler {

	private static MessageHandler handler = new MessageHandler();

	public static MessageHandler getHandler() {
		return handler;
	}

	public EmbedBuilder getDefaultEmbed(String header) {
		return new EmbedBuilder()
				.setAuthor("CookieBot - " + header, "https://minetopiasdb.nl",
						Main.getBot().getSelfUser().getAvatarUrl())
				.setFooter("MinetopiaSDB - Copyright 2019.", Main.getBot().getSelfUser().getAvatarUrl())
				.setColor(Color.CYAN);
	}

	// Eatcookie
	public EmbedBuilder getEatCookieProgressEmbed(User user, int secondsLeft) {
		return getDefaultEmbed("Eetcookie").addField("User", user.getName(), true)
				.addField("Tijd", secondsLeft + " sec.", true).addField("Status", "Cookie aan het opeten..", true)
				.addField("Prijs", "???", true);
	}

	public EmbedBuilder getEatCookieFinishEmbed(User user, String price) {
		return getDefaultEmbed("Eetcookie").addField("User", user.getName(), true)
				.addField("Status", "Cookie gegeten.", true).addField("Prijs", price, true)
				.addField("Nieuw aantal cookies", "" + CookieData.getInstance().getCookies(user.getIdLong()), true);
	}

	// Stealcookie
	public EmbedBuilder getStealCookieProgressEmbed(User user, User target, int secondsLeft) {
		return getDefaultEmbed("Steelcookie").addField("Doelwit", target.getName(), true)
				.addField("Tijd", "" + secondsLeft + " sec.", true).addField("Status", "Cookie aan het stelen...", true)
				.addField("Resultaat", "???", true);
	}

	public EmbedBuilder getStealCookieFinishEmbed(User user, User target, String result, String price) {
		return getDefaultEmbed("Steelcookie").addField("Doelwit", target.getName(), true)
				.addField("Status", result, true).addField("Resultaat", price, true);
	}

	// Givecookie
	public EmbedBuilder getGiveCookieEmbed(Member giver, Member receiver) {
		return getDefaultEmbed("GiveCookie").addField("Cookies van", giver.getEffectiveName(), true)
				.addField("Cookies voor", receiver.getEffectiveName(), true)
				.addField("Nieuw aantal cookies", "" + CookieData.getInstance().getCookies(receiver.getIdLong()), true);
	}
}
