package nl.minetopiasdb.cookiebot.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariDataSource;

import nl.minetopiasdb.cookiebot.utils.BotConfig;

public class HikariSQL {

	public static HikariSQL instance = null;
	private HikariDataSource hikari;

	public static HikariSQL getInstance() {
		if (instance == null) {
			instance = new HikariSQL();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		return getHikari().getConnection();
	}

	public HikariDataSource getHikari() {
		return hikari;
	}

	public void setup() {
		hikari = new HikariDataSource();
		hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
		hikari.addDataSourceProperty("serverTimezone", "UTC");
		hikari.addDataSourceProperty("serverName", BotConfig.getInstance().MYSQL_HOST);
		hikari.addDataSourceProperty("port", 3306);
		hikari.addDataSourceProperty("databaseName", BotConfig.getInstance().MYSQL_DATABASE);
		hikari.addDataSourceProperty("user", BotConfig.getInstance().MYSQL_USERNAME);
		hikari.addDataSourceProperty("password", BotConfig.getInstance().MYSQL_PASSWORD);
		hikari.setLeakDetectionThreshold(10000);
		hikari.setMaximumPoolSize(10);
		hikari.addDataSourceProperty("cachePrepStmts", "true");
		hikari.addDataSourceProperty("prepStmtCacheSize", "250");
		hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		hikari.setPoolName("SDB CookieBot");
		hikari.setConnectionTestQuery("SELECT 1");

		try (Connection conn = getConnection()) {
			Statement statement = conn.createStatement();
			statement.execute(
					"CREATE TABLE IF NOT EXISTS `CookieData` (`userId` bigint(18) NOT NULL PRIMARY KEY, `cookies` int(11) NOT NULL)");
			statement.execute(
					"CREATE TABLE IF NOT EXISTS `StockData` (`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY, `userId` bigint(18), `stocksymbol` varchar(5), `amount` int NOT NULL)");
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
