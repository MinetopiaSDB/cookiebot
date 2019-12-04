package nl.minetopiasdb.cookiebot.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
			PreparedStatement statement = conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `CookieData` (`userId` bigint(18) NOT NULL PRIMARY KEY, `cookies` int(11) NOT NULL)");
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
