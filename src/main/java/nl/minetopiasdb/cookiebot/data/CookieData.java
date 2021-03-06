package nl.minetopiasdb.cookiebot.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

	public int getCookies(long userId) {
		int cookies = 0;

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

	public void setCookies(long userId, int amount) {
		addUserDefaults(userId);
		try (Connection conn = HikariSQL.getInstance().getConnection()) {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE `CookieData` SET `cookies`=" + amount + " WHERE `userId`=?");
			ps.setLong(1, userId);
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

	public void addCookies(Long userId, int amount) {
		setCookies(userId, getCookies(userId) + amount);
	}

	public void removeCookies(Long userId, int amount) {
		setCookies(userId, getCookies(userId) - amount);
	}
}
