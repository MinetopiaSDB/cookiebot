package nl.minetopiasdb.cookiebot.utils;

import java.io.IOException;

import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

public class BotConfig {

	private static BotConfig instance;

	public String BOT_TOKEN, MYSQL_HOST, MYSQL_DATABASE, MYSQL_USERNAME, MYSQL_PASSWORD;
	public int MYSQL_PORT;
	public Long DONATOR_ROLE_ID, GUILD_ID, COOKIECHANNEL_ID;

	public static BotConfig getInstance() {
		if (instance == null) {
			instance = new BotConfig();
		}
		return instance;
	}

	public void initialise() {
		try {
			YamlFile file = new YamlFile("config.yml");
			if (file.exists()) {
				file.load();
			}

			file.addDefault("Bot.Token", "TYP-HIER-JOUW-BOTTOKEN");
			file.addDefault("Bot.DonatorRoleID", 381114554010173440l);
			file.addDefault("Bot.GuildID", 276296022106308609l);
			file.addDefault("Bot.CookieChannelId", -1l);

			file.addDefault("MySQL.Host", "127.0.0.1");
			file.addDefault("MySQL.Port", 3306);
			file.addDefault("MySQL.Database", "cookiebot");
			file.addDefault("MySQL.Username", "cookiebot");
			file.addDefault("MySQL.Password", "ikwilkoekjes");
			file.options().copyDefaults(true);
			file.save();

			BOT_TOKEN = file.getString("Bot.Token");
			MYSQL_HOST = file.getString("MySQL.Host");
			MYSQL_DATABASE = file.getString("MySQL.Database");
			MYSQL_USERNAME = file.getString("MySQL.Username");
			MYSQL_PASSWORD = file.getString("MySQL.Password");

			MYSQL_PORT = file.getInt("MySQL.Port");

			GUILD_ID = file.getLong("Bot.GuildID");
			DONATOR_ROLE_ID = file.getLong("Bot.DonatorRoleID");
			COOKIECHANNEL_ID = file.getLong("Bot.CookieChannelId");

		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
