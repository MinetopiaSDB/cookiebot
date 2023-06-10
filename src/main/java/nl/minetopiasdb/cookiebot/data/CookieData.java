package nl.minetopiasdb.cookiebot.data;

import nl.minetopiasdb.cookiebot.utils.BotConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CookieData {

	private static CookieData instance;

	public static CookieData getInstance() {
		if (instance == null) {
			instance = new CookieData();
		}
		return instance;
	}

	public void addUserDefaults(Long userId) {
		try (Connection conn = HikariSQL.getInstance().getConnection()) {
			PreparedStatement selectPs = conn.prepareStatement("SELECT `cookies` FROM `CookieData` WHERE userId=?");
			selectPs.setLong(1, userId);
			ResultSet r = selectPs.executeQuery();

			if (!r.next()) {
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO `CookieData` (`userId`, `cookies`) VALUES (?, ?)");
				ps.setLong(1, userId);
				ps.setInt(2, 0);
				ps.executeUpdate();
				ps.close();
			}

			selectPs.close();
			r.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public long getCookies(long userId) {
		long cookies = 0;

		try (Connection conn = HikariSQL.getInstance().getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT `cookies` FROM `CookieData` WHERE userId=?");
			ps.setLong(1, userId);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				cookies = r.getInt("cookies");
			}

			ps.close();
			r.close();
			return cookies;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getFormattedCookies(long userId) {
		return BotConfig.getInstance().format(getCookies(userId));
	}

	public void setCookies(long userId, long amount) {
		addUserDefaults(userId);
		try (Connection conn = HikariSQL.getInstance().getConnection()) {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE `CookieData` SET `cookies`=? WHERE `userId`=?");
			ps.setLong(1, amount);
			ps.setLong(2, userId);
			ps.executeUpdate();
			ps.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public HashMap<Long, Integer> getCookieTop() {
		LinkedHashMap<Long, Integer> cookieTop = new LinkedHashMap<>();
		try (Connection conn = HikariSQL.getInstance().getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM `CookieData` ORDER BY `cookies` DESC LIMIT 10");
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				cookieTop.put(rs.getLong("userId"), rs.getInt("cookies"));
			}
			
			rs.close();
			ps.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cookieTop;
	}

	public void addCookies(Long userId, long amount) {
		setCookies(userId, getCookies(userId) + amount);
	}

	public void removeCookies(Long userId, long amount) {
		setCookies(userId, getCookies(userId) - amount);
	}
}
