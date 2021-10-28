package nl.minetopiasdb.cookiebot.tasks;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import nl.minetopiasdb.cookiebot.Main;
import nl.minetopiasdb.cookiebot.data.CookieData;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;

public class EatCookieTask extends TimerTask {

	public HashMap<Long, Entry<InteractionHook, Integer>> currentlyEating = new HashMap<>();

	@Override
	public void run() {
		for (long userId : new ArrayList<>(currentlyEating.keySet())) {
			Entry<InteractionHook, Integer> entry = currentlyEating.get(userId);
			InteractionHook hook = entry.getKey();
			int seconds = entry.getValue();

			if (seconds > 1) {
				seconds--;

				hook.editOriginal("").setEmbeds(MessageHandler.getHandler()
						.getEatCookieProgressEmbed(Main.getBot().retrieveUserById(userId, false).complete(), seconds).build()).queue();

				currentlyEating.remove(userId);
				currentlyEating.put(userId, new AbstractMap.SimpleEntry<>(hook, seconds));
			} else {
				int price = -1;
				String priceMsg = "";

				int random = new Random().nextInt(4);

				if (random == 0) {
					price = 5;
					priceMsg = "5 cookies :cookie:";
				} else if (random == 1) {
					price = 10;
					priceMsg = "10 cookies :cookie::cookie::cookie:";
				} else if (random == 2) {
					price = -3;
					priceMsg = "-3 cookies :tired_face:";
				} else {
					price = 0;
					priceMsg = "Niks :tired_face: :tired_face: :tired_face: ";
				}

				currentlyEating.remove(userId);

				if (price != 0) {
					CookieData.getInstance().addCookies(userId, price);
				}
				
				hook.editOriginal("").setEmbeds(MessageHandler.getHandler()
						.getEatCookieFinishEmbed(Main.getBot().retrieveUserById(userId, false).complete(), priceMsg).build()).queue();
			}
		}
	}

	public HashMap<Long, Entry<InteractionHook, Integer>> getMap() {
		return currentlyEating;
	}

}
