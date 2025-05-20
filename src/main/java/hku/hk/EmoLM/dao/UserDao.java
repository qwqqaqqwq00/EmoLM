package hku.hk.EmoLM.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import hku.hk.EmoLM.entity.User;

import java.sql.Types;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 验证用户登录并获取角色
    public String authenticateUser(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(query,
                    new Object[]{username, password},
                    new int[]{Types.VARCHAR, Types.VARCHAR},
                    String.class);
        } catch (Exception e) {
            // 如果查询失败（如用户不存在或密码错误），返回null
            return null;
        }
    }

    // 注册新用户
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
            // 如果查询失败（如用户不存在或密码错误），返回null
            return null;
        }
    }
}