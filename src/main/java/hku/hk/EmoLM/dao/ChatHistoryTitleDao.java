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
        String sql = "SELECT * FROM chat_history_titles WHERE uid = ?";
        return jdbcTemplate.query(sql, new Object[]{uid}, new int[]{java.sql.Types.INTEGER}, new BeanPropertyRowMapper<>(ChatHistoryTitleEntity.class));
    }

    public void addChatHistoryTitle(ChatHistoryTitleEntity chatHistoryTitle) {
        String sql = "INSERT INTO chat_history_titles (hid, uid, title) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, chatHistoryTitle.getHid(), chatHistoryTitle.getUid(), chatHistoryTitle.getTitle());
    }
}