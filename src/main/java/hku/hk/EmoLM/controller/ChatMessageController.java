package hku.hk.EmoLM.controller;

import hku.hk.EmoLM.entity.ChatHistoryEntity;
import hku.hk.EmoLM.entity.ChatHistoryTitleEntity;
import hku.hk.EmoLM.service.ChatHistoryService;
import hku.hk.EmoLM.service.ChatHistoryTitleService;
import hku.hk.EmoLM.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private ChatHistoryTitleService chatHistoryTitleService;

    @Autowired
    private UserService userService;

    @PostMapping("/history/titles")
    public ResponseEntity<?> getChatHistoryTitles(@RequestHeader("Authorization") String token) {
        Object uid = userService.UserToken2Uid(token);
        if(uid != null){
            List<ChatHistoryTitleEntity> titles = chatHistoryTitleService.getChatHistoryTitles((Integer) uid);
            return ResponseEntity.ok(titles);
        }
        return ResponseEntity.ok(Map.of("error", "Not logged in"));
    }

    /**
     * get chat history
     */
    @PostMapping("/history")
    public ResponseEntity<?> getChatHistory(@RequestParam int hid, @RequestHeader("Authorization") String token) {
        Object uid_raw = userService.UserToken2Uid(token);
        if(uid_raw != null){
            int uid = (int) uid_raw;
            if(hid == 0) {
                hid = chatHistoryService.getLastHid(uid);
            }
            List<ChatHistoryEntity> messages = chatHistoryService.getChatHistory(hid, uid);
            if (messages != null) {
                return ResponseEntity.ok(messages);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(Map.of("error", "Not logged in"));
    }

    /**
     * create new chat history
     */
    @PostMapping("/create")
    public ResponseEntity<?> createChatHistory(@RequestHeader("Authorization") String token) {
        Object uid_raw = userService.UserToken2Uid(token);
        if(uid_raw != null){
            int uid = (int) uid_raw;
            // create new chat history
            int hid = chatHistoryService.createChatHistory(uid);
            chatHistoryTitleService.addChatHistoryTitle(hid, uid);

            // return hid
            return ResponseEntity.ok(Map.of("hid", hid));
        }
        return ResponseEntity.ok(Map.of("error", "Not logged in"));
    }

    /**
     * send message and generate response
     */
    @PostMapping(value = "/generate", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> generateMessage(
            @RequestParam String message,
            @RequestParam(required = false) int hid,
            @RequestHeader("Authorization") String token) {
        try {
            Object uid_raw = userService.UserToken2Uid(token);
            if(uid_raw != null){
                int uid = (int) uid_raw;
                if (hid == 0) {
                    hid = chatHistoryService.getLastHid(uid);
                }
                Map<String, Object> response = Map.of(
                        "message", "This is a generated response to: " + message
                );

                chatHistoryService.addMessage(hid, message, "human", uid);
                chatHistoryService.addMessage(hid, response.get("message").toString(), "assistant", uid);

                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(Map.of("error", "Not logged in"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error processing message"));
        }
    }
}