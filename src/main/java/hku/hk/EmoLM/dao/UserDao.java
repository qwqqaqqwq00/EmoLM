package hku.hk.EmoLM.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import hku.hk.EmoLM.entity.User;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 验证用户登录
    public boolean authenticateUser(String username, String password) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{username, password}, Integer.class) > 0;
    }

    // 注册新用户
    public boolean registerUser(String username, String password, String email) {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        return jdbcTemplate.update(query, username, password, email) > 0;
    }
}