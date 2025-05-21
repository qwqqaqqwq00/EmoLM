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

    public List<ChatHistoryEntity> getChatHistory(int hid, int uid) { // Modified method signature, added uid parameter
        return chatHistoryDao.getChatHistory(hid, uid);
    }

    /**
     * Create new chat history
     */
    public int createChatHistory(int uid) {
        // Initialize a new chat history
        ChatHistoryEntity chatHistory = new ChatHistoryEntity();
        int hid = chatHistoryDao.getLastHid(uid);
        chatHistory.setHid(hid + 1);
        chatHistory.setTimestamp(new Date());
        chatHistory.setUid(uid);
        chatHistory.setMessage("Hello, what can I help you?");
        chatHistory.setRole("assistant");
        chatHistoryDao.addMessage(chatHistory);
        return chatHistory.getHid();
    }

    public int getLastHid(int uid) {
        return chatHistoryDao.getLastHid(uid);
    }

    public void addMessage(int hid, String message, String role, int uid) { // Modified method signature, added uid parameter
        ChatHistoryEntity chatHistory = new ChatHistoryEntity();
        chatHistory.setHid(hid);
        chatHistory.setMessage(message);
        chatHistory.setRole(role);
        chatHistory.setTimestamp(new Date());
        chatHistory.setUid(uid); // Set uid
        chatHistoryDao.addMessage(chatHistory);
    }
}