package nl.minetopiasdb.cookiebot.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.Message;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;

public class StealCookieTask extends TimerTask {

	public HashMap<Long, StealData> currentlyEating = new HashMap<>();

	@Override
	public void run() {
		for (long userId : new ArrayList<>(currentlyEating.keySet())) {
			StealData stealUser = currentlyEating.get(userId);
			Message msg = stealUser.getMessage();
			int seconds = stealUser.getSeconds();
			long targetId = stealUser.getTarget();

			if (seconds > 1) {
				seconds--;

				msg.editMessage(MessageHandler.getHandler()
						.getStealCookieProgressEmbed(Main.getBot().retrieveUserById(userId, false).complete(),
								Main.getBot().retrieveUserById(targetId, false).complete(), seconds)
						.build()).queue();

				currentlyEating.remove(userId);
				currentlyEating.put(userId, new StealData(msg, seconds, targetId));
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
				} else if (random == 2) {
					CookieData.getInstance().removeCookies(userId, 2);
					priceStatus = "Mislukt...";
					priceMsg = "Je bent betrapt door de politie, je bent 2 cookies verloren.. :police_officer:";
				}

				currentlyEating.remove(userId);
				msg.editMessage(MessageHandler.getHandler()
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

		private Message message;
		private int seconds;
		private long target;

		public StealData(Message message, int seconds, long target) {
			this.message = message;
			this.seconds = seconds;
			this.target = target;
		}

		public Message getMessage() {
			return message;
		}

		public int getSeconds() {
			return seconds;
		}

		public long getTarget() {
			return target;
		}
	}
}
