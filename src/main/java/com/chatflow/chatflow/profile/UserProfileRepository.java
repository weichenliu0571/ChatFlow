package com.chatflow.chatflow.profile;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileRepository {
  private final JdbcTemplate jdbc;
  public UserProfileRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public void setAvatarUrl(String username, String url) {
    jdbc.update("UPDATE users SET avatar = ? WHERE username = ?", url, username);
  }

  public String getAvatarUrl(String username) {
    return jdbc.query("SELECT avatar FROM users WHERE username = ?",
      rs -> rs.next() ? rs.getString(1) : null, username);
  }
}
