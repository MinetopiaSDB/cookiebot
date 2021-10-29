package nl.minetopiasdb.cookiebot;

import java.util.*;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.swing.text.html.Option;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
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
import nl.minetopiasdb.cookiebot.data.stocks.FinnhubAPI;
import nl.minetopiasdb.cookiebot.data.stocks.StockUserData;
import nl.minetopiasdb.cookiebot.listeners.CommandListener;
import nl.minetopiasdb.cookiebot.tasks.EatCookieTask;
import nl.minetopiasdb.cookiebot.tasks.StealCookieTask;
import nl.minetopiasdb.cookiebot.tasks.StockTask;
import nl.minetopiasdb.cookiebot.utils.BotConfig;
import nl.minetopiasdb.cookiebot.utils.MessageHandler;
import nl.minetopiasdb.cookiebot.utils.commands.CommandFactory;

public class Main {

	private static FinnhubAPI finnhubAPI;
	private static JDA jda;
	private static EatCookieTask eatCookieTask;
	private static StealCookieTask stealCookieTask;

	public static void main(String[] args) {
		BotConfig.getInstance().initialise();

		if (BotConfig.getInstance().BOT_TOKEN.equals("TYP-HIER-JOUW-BOTTOKEN")) {
			System.out.println("Please change the bot token before running the MinetopiaSDB CookieBot!");
			return;
		}

		try {
			jda = JDABuilder.create(BotConfig.getInstance().BOT_TOKEN, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
					.disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
					.build();
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
		timer.scheduleAtFixedRate(eatCookieTask, 1000L, 1000L);
		timer.scheduleAtFixedRate(stealCookieTask, 1000L, 1000L);
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				StealcookieCooldown.getInstance().manageCooldowns();
				EatcookieCooldown.getInstance().manageCooldowns();
				GivecookieCooldown.getInstance().manageCooldowns();
			}
		}, 60 * 1000L, 60 * 1000L);

		BotConfig bc = BotConfig.getInstance();
		if (bc.STOCKS_ENABLED) {
			if (bc.FINNHUB_KEY.equals("LEUKEAPIKEYZEG")) {
				System.out.println("Please request a free Finnhub API key at finnhub.io before enabling stocks!");
			} else {
				finnhubAPI = new FinnhubAPI(bc.FINNHUB_KEY);
				timer.scheduleAtFixedRate(new StockTask(), 0L, 1000 * 60 * 4);
				StockUserData.getInstance().pullFromDatabase();
				CommandFactory.getInstance().registerCommand("aandelen",
						"Bekijk alle beschikbare aandelen en hun waarde.",
						new StockCMD());
				CommandFactory.getInstance().registerCommand("portfolio",
						"Bekijk welke aandelen jij nu hebt en hoeveel deze waard zijn", new PortfolioCMD());

				// Create OptionData and add every stock
				OptionData stockOption = new OptionData(OptionType.STRING, "aandeel",
						"Het aandeel waarop jij deze actie wilt verrichten", true);
				BotConfig.getInstance().stocks.forEach((key, value) -> stockOption.addChoice(value, key));

				CommandFactory.getInstance().registerCommand("koopaandeel",
						"Koop aandelen", new PurchaseStockCMD(), stockOption,
						new OptionData(OptionType.INTEGER, "hoeveelheid",
								"De hoeveelheid aandelen die je wilt kopen"));

				CommandFactory.getInstance().registerCommand("verkoopaandeel",
						"Verkoop aandelen", new SellStockCMD(), stockOption,
						new OptionData(OptionType.INTEGER, "hoeveelheid",
								"De hoeveelheid aandelen die je wilt verkopen"));
			}
		}
		CommandFactory.getInstance().registerCommand("cookies",
				"Bekijk de hoeveelheid koekjes van jezelf en andere serverleden", new CookiesCMD(),
				new OptionData(OptionType.USER, "user", "Persoon van wie jij de hoeveelheid koekjes wilt zien"));

		CommandFactory.getInstance().registerCommand("givecookie",
				"Geef iemand vijf koekjes", new GivecookieCMD(),
				new OptionData(OptionType.USER, "user", "Persoon wie jij vijf koekjes wilt geven", true));

		CommandFactory.getInstance().registerCommand("eetcookie",
				"Eet een koekje op en maak kans op prijzen!", new EatcookieCMD());

		CommandFactory.getInstance().registerCommand("paycookie",
				"Geef iemand koekjes kado", new PaycookieCMD(),
				new OptionData(OptionType.USER, "user", "Persoon wie jij koekjes wilt geven", true),
				new OptionData(OptionType.INTEGER, "hoeveelheid", "De hoeveelheid koekjes die je wilt geven", true));

		CommandFactory.getInstance().registerCommand("steelcookie",
				"Probeer koekjes van iemand te stelen", new StealcookieCMD(),
				new OptionData(OptionType.USER, "user", "Persoon van wie jij koekjes wilt stelen", true));

		CommandFactory.getInstance().registerCommand("cookietop",
				"Kom erachter wie de meeste cookies heeft!",
				new CookietopCMD());

		// Give hints to users that still use the old commands
		Set<String> OLD_COMMANDS = Set.of("!aandelen", "!portfolio", "!koopaandeel", "!verkoopaandeel", "!cookies",
				"!givecookie", "!eetcookie", "!paycookie", "!steelcookie", "!cookietop");
		jda.addEventListener(new ListenerAdapter() {
			@Override
			public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
				if (BotConfig.getInstance().COOKIECHANNEL_ID != -1L
						&& BotConfig.getInstance().COOKIECHANNEL_ID != event.getChannel().getIdLong()) {
					return;
				}
				String message = event.getMessage().getContentRaw();
				String[] messageArguments = message.toLowerCase().split(" ");
				if (OLD_COMMANDS.contains(messageArguments[0])) {
					event.getChannel().sendMessageEmbeds(
									MessageHandler.getHandler().getDefaultEmbed("Tip!")
											.setDescription("Je moet **/" + messageArguments[0].substring(1) + "**" +
													" gebruiken om dit commando uit te voeren")
											.build())
							.queue();
				}

			}
		});
	}

	public static JDA getBot() {
		return jda;
	}

	public static FinnhubAPI getFinnhubAPI() {
		return finnhubAPI;
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
