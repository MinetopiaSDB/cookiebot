package nl.minetopiasdb.cookiebot.cooldowns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Cooldown {
	
	protected HashMap<Long, Long> getCooldownMap() {
		return null;
	}
	
	/**
	 * Get the default cooldown length in minutes
	 * 
	 * @return cooldown length in minutes
	 */
	public int getCooldownLength() {
		return -1;
	}

	/**
	 * Add a user to the cooldown
	 * 
	 * @param userId user id
	 */
	public void addUserToCooldown(long userId) {
		getCooldownMap().put(userId, System.currentTimeMillis() + (getCooldownLength() * 60 * 1000));
	}

	/**
	 * Check if user is in cooldown
	 * @param userId user id
	 * @return true if user has cooldown
	 */
	public boolean hasCooldown(long userId) {
		return getCooldownMap().containsKey(userId);
	}
	
	/**
	 * Get a formatted string that represents the time left
	 * @param userId
	 * @return
	 */
	public String getTimeLeft(long userId) {
		if (!hasCooldown(userId)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
		
		return sdf.format(new Date(getCooldownMap().get(userId)));
	}
	
	/**
	 * Run the cooldown task
	 */
	@SuppressWarnings("unchecked")
	public void manageCooldowns() {
		((HashMap<Long, Long>) getCooldownMap().clone()).keySet().forEach(userId -> {
			if (System.currentTimeMillis() > getCooldownMap().get(userId)) {
				getCooldownMap().remove(userId);
			}

		});

	}

}
