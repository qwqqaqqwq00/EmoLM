package hku.hk.EmoLM.dao;

import hku.hk.EmoLM.entity.ChatHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatHistoryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ChatHistoryEntity> getChatHistory(int hid, int uid) { // 修改方法签名，增加 uid 参数
        String sql = "SELECT * FROM chat_history WHERE hid = ? AND uid = ? ORDER BY id ";
        return jdbcTemplate.query(sql, new Object[]{hid, uid}, new BeanPropertyRowMapper<>(ChatHistoryEntity.class));
    }

    public void addMessage(ChatHistoryEntity chatHistory) {
        String sql = "INSERT INTO chat_history (hid, message, role, timestamp, uid) VALUES (?, ?, ?, ?, ?)"; // 修改 SQL，增加 uid 字段
        jdbcTemplate.update(sql, chatHistory.getHid(), chatHistory.getMessage(), chatHistory.getRole(), chatHistory.getTimestamp(), chatHistory.getUid());
    }
}