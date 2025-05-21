package hku.hk.EmoLM.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.Types;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // authenticate user and get role
    public String authenticateUser(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(query,
                    new Object[]{username, password},
                    new int[]{Types.VARCHAR, Types.VARCHAR},
                    String.class);
        } catch (Exception e) {
            // if query fails (e.g. user does not exist or password is incorrect), return null
            return null;
        }
    }

    // register new user
    public boolean registerUser(String username, String password, String email) {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        return jdbcTemplate.update(query, username, password, email) > 0;
    }

    public Object getUserIdByUsername(String username, String password) {
        String  query = "SELECT id FROM users WHERE username = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(query,
                    new Object[]{username, password},
                    new int[]{Types.VARCHAR, Types.VARCHAR},
                    Integer.class);
        } catch (Exception e) {
            // if query fails (e.g. user does not exist or password is incorrect), return null
            return null;
        }
    }
}