package hku.hk.EmoLM.service;

import hku.hk.EmoLM.dao.ChatHistoryDao;
import hku.hk.EmoLM.entity.ChatHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatHistoryService {

    @Autowired
    private ChatHistoryDao chatHistoryDao;

    public List<ChatHistoryEntity> getChatHistory(int hid, int uid) { // 修改方法签名，增加 uid 参数
        return chatHistoryDao.getChatHistory(hid, uid);
    }

    public void addMessage(int hid, String message, String role, int uid) { // 修改方法签名，增加 uid 参数
        ChatHistoryEntity chatHistory = new ChatHistoryEntity();
        chatHistory.setHid(hid);
        chatHistory.setMessage(message);
        chatHistory.setRole(role);
        chatHistory.setTimestamp(new Date());
        chatHistory.setUid(uid); // 设置 uid
        chatHistoryDao.addMessage(chatHistory);
    }
}