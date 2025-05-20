package hku.hk.EmoLM.controller;

import hku.hk.EmoLM.entity.ChatHistoryEntity;
import hku.hk.EmoLM.entity.ChatHistoryTitleEntity;
import hku.hk.EmoLM.service.ChatHistoryService;
import hku.hk.EmoLM.service.ChatHistoryTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private ChatHistoryTitleService chatHistoryTitleService;

    @Autowired
    private AuthController authController;

    @PostMapping("/history/titles")
    public ResponseEntity<?> getChatHistoryTitles(@RequestParam String token) {
        ResponseEntity<?> uidResponse = authController.getUidFromToken(token);
        if (uidResponse.getStatusCode().is2xxSuccessful()) {
            int uid = (int) ((Map<String, Object>) Objects.requireNonNull(uidResponse.getBody())).get("uid");
            List<ChatHistoryTitleEntity> titles = chatHistoryTitleService.getChatHistoryTitles(uid);
            return ResponseEntity.ok(titles);
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "无效的 token"));
        }
    }

    /**
     * 获取聊天历史记录
     */
    @PostMapping("/history")
    public ResponseEntity<?> getChatHistory(@RequestParam int hid, @RequestParam String token) {
        // 通过 token 获取 uid
        ResponseEntity<?> uidResponse = authController.getUidFromToken(token);
        if (uidResponse.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = (Map<String, Object>) uidResponse.getBody();
            if(body == null || !body.containsKey("uid")){
                return ResponseEntity.badRequest().body(Map.of("error", body.toString()));
            }
            int uid = (int) body.get("uid");
            List<ChatHistoryEntity> messages = chatHistoryService.getChatHistory(hid, uid);
            if (messages != null) {
                return ResponseEntity.ok(messages);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "无效的 token"));
        }
    }

    /**
     * 创建新的聊天历史记录
     */
    @PostMapping("/create")
    public ResponseEntity<?> createChatHistory(@RequestParam String token) {
        ResponseEntity<?> uidResponse = authController.getUidFromToken(token);
        if (uidResponse.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = (Map<String, Object>) uidResponse.getBody();
            if (body == null || !body.containsKey("uid")) {
                return ResponseEntity.badRequest().body(Map.of("error", "无效的 token"));
            }
            int uid = (int) body.get("uid");

            // 创建新的聊天历史记录
            int hid = chatHistoryService.createChatHistory(uid);
            chatHistoryTitleService.addChatHistoryTitle(hid, uid);

            // 返回生成的 hid
            return ResponseEntity.ok(Map.of("hid", hid));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "无效的 token"));
        }
    }

    /**
     * 发送消息并生成回复
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateMessage(
            @RequestParam String message,
            @RequestParam(required = false) List<String> files,
            @RequestParam String token,
            @RequestParam(required = false) int hid) {
        try {
            ResponseEntity<?> uidResponse = authController.getUidFromToken(token);
            if (uidResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = (Map<String, Object>) uidResponse.getBody();
                if (body == null || !body.containsKey("uid")) {
                    return ResponseEntity.badRequest().body(Map.of("error", "无效的 token"));
                }
                int uid = (int) body.get("uid");

                // 模拟生成回复
                Map<String, Object> response = Map.of(
                        "message", "This is a generated response to: " + message,
                        "files", files != null ? files : List.of()
                );

                // 将消息添加到聊天历史中
                if(hid == 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "无效的 hid"));
                }
                chatHistoryService.addMessage(hid, message, "human", uid);
                chatHistoryService.addMessage(hid, response.get("message").toString(), "assistant", uid);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "无效的 token"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "处理消息时发生错误"));
        }
    }
}