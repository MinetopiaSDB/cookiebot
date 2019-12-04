package nl.minetopiasdb.cookiebot.cooldowns;

import java.util.HashMap;

public class EatcookieCooldown extends Cooldown {

	private static EatcookieCooldown instance;
	private HashMap<Long, Long> cooldownMap = new HashMap<>();

	/**
	 * Get a cooldown instance
	 * 
	 * @return instance
	 */
	public static EatcookieCooldown getInstance() {
		if (instance == null) {
			instance = new EatcookieCooldown();
		}
		return instance;
	}
	
	/**
	 * Get the cooldown hashmap
	 * 
	 * @return hashmap with userid and expiry respectively
	 */
	@Override
	protected HashMap<Long, Long> getCooldownMap() {
		return cooldownMap;
	}

	@Override
	public int getCooldownLength() {
		return 60;
	}
}
