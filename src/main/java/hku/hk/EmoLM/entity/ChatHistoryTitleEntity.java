package hku.hk.EmoLM.entity;

public class ChatHistoryTitleEntity {
    private int id;
    private int hid;
    private String title;
    private int uid;

    // 添加无参构造函数
    public ChatHistoryTitleEntity() {
    }

    public ChatHistoryTitleEntity(int hid, int uid, String newChat) {
        this.hid = hid;
        this.uid = uid;
        this.title = newChat;
    }

    // Getters and Setters
    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}