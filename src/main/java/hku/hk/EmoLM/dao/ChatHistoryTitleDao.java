package hku.hk.EmoLM.dao;

import hku.hk.EmoLM.entity.ChatHistoryTitleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatHistoryTitleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ChatHistoryTitleEntity> getChatHistoryTitles(int uid) {
        String sql = "SELECT hid, title FROM chat_history_titles WHERE uid = ?";
        return jdbcTemplate.query(sql, new Object[]{uid}, new BeanPropertyRowMapper<>(ChatHistoryTitleEntity.class));
    }
}