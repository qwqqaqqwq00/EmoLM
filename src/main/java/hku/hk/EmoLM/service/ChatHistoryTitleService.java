package hku.hk.EmoLM.service;

import hku.hk.EmoLM.dao.ChatHistoryTitleDao;
import hku.hk.EmoLM.entity.ChatHistoryTitleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryTitleService {

    @Autowired
    private ChatHistoryTitleDao chatHistoryTitleDao;

    public List<ChatHistoryTitleEntity> getChatHistoryTitles(int uid) {
        return chatHistoryTitleDao.getChatHistoryTitles(uid);
    }
}