package nl.minetopiasdb.cookiebot.tasks;

import java.util.*;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;

public class StealCookieTask extends TimerTask {

	public HashMap<Long, StealData> currentlyEating = new HashMap<>();

	@Override
	public void run() {
		for (long userId : new ArrayList<>(currentlyEating.keySet())) {
			StealData stealUser = currentlyEating.get(userId);
			InteractionHook hook = stealUser.getInteractionHook();
			int seconds = stealUser.getSeconds();
			long targetId = stealUser.getTarget();
			User user = Main.getBot().retrieveUserById(userId, false).complete();
			User target = Main.getBot().retrieveUserById(targetId, false).complete();

			if (seconds > 1) {
				seconds--;

				hook.editOriginal(target.getAsMention()).setEmbeds(MessageHandler.getHandler()
						.getStealCookieProgressEmbed(user, target, seconds)
						.build()).queue();

				currentlyEating.remove(userId);
				currentlyEating.put(userId, new StealData(hook, seconds, targetId));
			} else {
				String priceMsg = "", priceStatus = "";

				int random = new Random().nextInt(3);

				if (random == 0) {
					CookieData.getInstance().addCookies(userId, 3);
					CookieData.getInstance().removeCookies(targetId, 3);

					priceStatus = "Gelukt!!!";
					priceMsg = "Je hebt succesvol 3 cookies gestolen!! :smiling_imp::smiling_imp::smiling_imp:";
				} else if (random == 1) {
					CookieData.getInstance().removeCookies(userId, 3);
					CookieData.getInstance().addCookies(targetId, 3);

					priceStatus = "Mislukt...";
					priceMsg = "Je bent 3 cookies verloren aan de ander! :grimacing:";
				} else {
					CookieData.getInstance().removeCookies(userId, 2);
					priceStatus = "Mislukt...";
					priceMsg = "Je bent betrapt door de politie, je bent 2 cookies verloren.. :police_officer:";
				}

				currentlyEating.remove(userId);
				hook.editOriginal(target.getAsMention()).setEmbeds(MessageHandler.getHandler()
						.getStealCookieFinishEmbed(Main.getBot().retrieveUserById(userId, false).complete(),
								Main.getBot().retrieveUserById(targetId, false).complete(), priceStatus, priceMsg)
						.build()).queue();
			}
		}
	}

	public HashMap<Long, StealData> getMap() {
		return currentlyEating;
	}

	public static class StealData {

		private final InteractionHook hook;
		private final int seconds;
		private final long target;

		public StealData(InteractionHook hook, int seconds, long target) {
			this.hook = hook;
			this.seconds = seconds;
			this.target = target;
		}

		public InteractionHook getInteractionHook() {
			return hook;
		}

		public int getSeconds() {
			return seconds;
		}

		public long getTarget() {
			return target;
		}
	}
}
