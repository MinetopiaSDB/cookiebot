package nl.minetopiasdb.cookiebot.data.stocks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import nl.minetopiasdb.cookiebot.data.HikariSQL;

public class StockUserData {

	private static StockUserData instance;
	private HashMap<String, Integer> cache = new HashMap<>();

	public static StockUserData getInstance() {
		if (instance == null) {
			instance = new StockUserData();
		}
		return instance;
	}

	// `id` int NOT NULL PRIMARY KEY, `userId` bigint(18), `stocksymbol` varchar(5),
	// `amount` int NOT NULL

	public void pullFromDatabase() {
		cache.clear();
		try (Connection conn = HikariSQL.getInstance().getConnection()) {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT userId, stocksymbol, amount FROM StockData");
			while (rs.next()) {
				cache.put(rs.getLong("userId") + "." + rs.getString("stocksymbol"), rs.getInt("amount"));
			}
			rs.close();
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public int getStocks(long user, String symbol) {
		if (!cache.containsKey(user + "." + symbol)) {
			return 0;
		}
		return cache.get(user + "." + symbol);
	}

	public void setStocks(long user, String symbol, int amount) {
		cache.remove(user + "." + symbol);
		if (amount != 0) {
			cache.put(user + "." + symbol, amount);
		}
		try (Connection conn = HikariSQL.getInstance().getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM StockData WHERE userId=? AND stocksymbol=?");
			ps.setLong(1, user);
			ps.setString(2, symbol);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				PreparedStatement update = conn
						.prepareStatement("UPDATE StockData SET amount=? WHERE userId=? AND stocksymbol=?");
				update.setInt(1, amount);
				update.setLong(2, user);
				update.setString(3, symbol);
				update.execute();
				update.close();
			} else {
				PreparedStatement insert = conn
						.prepareStatement("INSERT INTO StockData (userId, stocksymbol, amount) VALUES (?, ?, ?)");
				insert.setLong(1, user);
				insert.setString(2, symbol);
				insert.setInt(3, amount);
				insert.execute();
				insert.close();
			}
			rs.close();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
