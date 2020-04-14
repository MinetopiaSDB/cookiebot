package nl.minetopiasdb.cookiebot;

import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import nl.minetopiasdb.cookiebot.commands.CookiesCMD;
import nl.minetopiasdb.cookiebot.commands.CookietopCMD;
import nl.minetopiasdb.cookiebot.commands.EatcookieCMD;
import nl.minetopiasdb.cookiebot.commands.GivecookieCMD;
import nl.minetopiasdb.cookiebot.commands.PaycookieCMD;
import nl.minetopiasdb.cookiebot.commands.StealcookieCMD;
import nl.minetopiasdb.cookiebot.commands.stocks.PortfolioCMD;
import nl.minetopiasdb.cookiebot.commands.stocks.PurchaseStockCMD;
import nl.minetopiasdb.cookiebot.commands.stocks.SellStockCMD;
import nl.minetopiasdb.cookiebot.commands.stocks.StockCMD;
import nl.minetopiasdb.cookiebot.cooldowns.EatcookieCooldown;
import nl.minetopiasdb.cookiebot.cooldowns.GivecookieCooldown;
import nl.minetopiasdb.cookiebot.cooldowns.StealcookieCooldown;
import nl.minetopiasdb.cookiebot.data.HikariSQL;
import nl.minetopiasdb.cookiebot.data.stocks.StockUserData;
import nl.minetopiasdb.cookiebot.listeners.CommandListener;
import nl.minetopiasdb.cookiebot.tasks.EatCookieTask;
import nl.minetopiasdb.cookiebot.tasks.StealCookieTask;
import nl.minetopiasdb.cookiebot.tasks.StockTask;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.commands.CommandFactory;

public class Main {

	private static JDA jda;
	private static EatCookieTask eatCookieTask;
	private static StealCookieTask stealCookieTask;
	public static boolean TESTED = true;

	public static void main(String[] args) {
		BotConfig.getInstance().initialise();

		if (BotConfig.getInstance().BOT_TOKEN.equals("TYP-HIER-JOUW-BOTTOKEN")) {
			System.out.println("Please change the bot token before running the MinetopiaSDB CookieBot!");
			return;
		}

		try {
			jda = new JDABuilder(BotConfig.getInstance().BOT_TOKEN).build();
		} catch (LoginException e) {
			e.printStackTrace();
		}

		try {
			jda.awaitReady();
		} catch (InterruptedException e) {
		}

		System.out.println("Loading MinetopiaSDB CookieBot on guild with ID " + getServerId());
		HikariSQL.getInstance().setup();

		jda.addEventListener(new CommandListener());

		eatCookieTask = new EatCookieTask();
		stealCookieTask = new StealCookieTask();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(eatCookieTask, 1000l, 1000l);
		timer.scheduleAtFixedRate(stealCookieTask, 1000l, 1000l);
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				StealcookieCooldown.getInstance().manageCooldowns();
				EatcookieCooldown.getInstance().manageCooldowns();
				GivecookieCooldown.getInstance().manageCooldowns();
			}
		}, 60 * 1000l, 60 * 1000l);

		if (BotConfig.getInstance().STOCKS_ENABLED) {
			if (BotConfig.getInstance().FINNHUB_KEY.equals("LEUKEAPIKEYZEG")) {
				System.out.println("Please request a free Finnhub API key at finnhub.io before enabling stocks!");
			} else {
				timer.scheduleAtFixedRate(new StockTask(), 0l, 1000 * 60 * 3);
				StockUserData.getInstance().pullFromDatabase();
				CommandFactory.getInstance().registerCommand("!aandelen", new StockCMD());
				CommandFactory.getInstance().registerCommand("!portfolio", new PortfolioCMD());
				CommandFactory.getInstance().registerCommand("!koopaandeel", new PurchaseStockCMD());
				CommandFactory.getInstance().registerCommand("!verkoopaandeel", new SellStockCMD());
			}
		}
		CommandFactory.getInstance().registerCommand("!cookies", new CookiesCMD());
		CommandFactory.getInstance().registerCommand("!givecookie", new GivecookieCMD());
		CommandFactory.getInstance().registerCommand("!eetcookie", new EatcookieCMD());
		CommandFactory.getInstance().registerCommand("!paycookie", new PaycookieCMD());
		CommandFactory.getInstance().registerCommand("!steelcookie", new StealcookieCMD());
		CommandFactory.getInstance().registerCommand("!cookietop", new CookietopCMD());
	}

	public static JDA getBot() {
		return jda;
	}

	public static Guild getGuild() {
		return getBot().getGuildById(getServerId());
	}

	public static long getServerId() {
		return BotConfig.getInstance().GUILD_ID;
	}

	public static EatCookieTask getEatCookieTask() {
		return eatCookieTask;
	}

	public static StealCookieTask getStealCookieTask() {
		return stealCookieTask;
	}
}
