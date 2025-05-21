package hku.hk.EmoLM.dao;

import hku.hk.EmoLM.entity.ChatHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Types;

import java.util.List;

@Repository
public class ChatHistoryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ChatHistoryEntity> getChatHistory(int hid, int uid) { // Modified method signature, added uid parameter
        String sql = "SELECT * FROM chat_history WHERE hid = ? AND uid = ? ORDER BY id ";
        // Explicitly specify parameter types to prevent SQL injection
        return jdbcTemplate.query(sql, new Object[]{hid, uid}, new int[]{java.sql.Types.INTEGER, java.sql.Types.INTEGER}, new BeanPropertyRowMapper<>(ChatHistoryEntity.class));
    }

    public void addMessage(ChatHistoryEntity chatHistory) {
        String sql = "INSERT INTO chat_history (hid, message, role, timestamp, uid) VALUES (?, ?, ?, ?, ?)"; // Modified SQL, added uid field
        jdbcTemplate.update(sql, chatHistory.getHid(), chatHistory.getMessage(), chatHistory.getRole(), chatHistory.getTimestamp(), chatHistory.getUid());
    }

    public int getLastHid(int uid) {
        String sql = "SELECT MAX(hid) FROM chat_history WHERE uid = ?";
        try {
            Integer result = jdbcTemplate.queryForObject(sql, new Object[]{uid}, new int[]{Types.INTEGER}, Integer.class);
            return (result != null) ? result : 1;
        } catch (Exception e) {
            return 1;
        }
    }
}