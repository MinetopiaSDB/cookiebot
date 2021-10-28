package nl.minetopiasdb.cookiebot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.BotCommand;
import nl.minetopiasdb.cookiebot.utils.commands.Command;

public class PaycookieCMD {/*implements BotCommand {

	@Override*/
	public void execute(Command cmd, String[] args, Message msg) {
		if (msg.getMentionedMembers().size() != 1 || args.length < 2) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Fout, gebruik: **!paycookie <@User> <Amount>!**").build()).queue();
			return;
		}
		int cookies = -1;
		try {
			cookies = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Fout, gebruik: **!paycookie <@User> <Amount>!**").build()).queue();
			return;
		}
		if (msg.getMentionedMembers().get(0).getIdLong() == msg.getAuthor().getIdLong()) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Je kunt geen koekjes aan jezelf betalen!").build()).queue();
			return;
		}
		if (cookies <= 0) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Fout, gebruik: **!paycookie <@User> <Amount>!**").build()).queue();
			return;
		}
		if (CookieData.getInstance().getCookies(msg.getAuthor().getIdLong()) < cookies) {
			msg.getChannel().sendMessage(MessageHandler.getHandler().getDefaultEmbed("Error")
					.setDescription("Je hebt niet genoeg cookies om dat te doen!").build()).queue();
			return;
		}
		Member target = msg.getMentionedMembers().get(0);

		CookieData.getInstance().removeCookies(msg.getAuthor().getIdLong(), cookies);
		CookieData.getInstance().addCookies(target.getIdLong(), cookies);

		msg.getChannel()
				.sendMessage(MessageHandler.getHandler().getDefaultEmbed("PayCookie")
						.addField("Cookies van", Main.getGuild().getMember(msg.getAuthor()).getEffectiveName(), true)
						.addField("Cookies voor", target.getEffectiveName(), true)
						.addField("Nieuwe aantal cookies van " + target.getEffectiveName(),
								CookieData.getInstance().getCookies(target.getIdLong()) + " cookies", true)
						.addField("Hoeveelheid cookies", cookies + " cookies", true).build())
				.queue();
	}
}
