package hku.hk.EmoLM.entity;

import java.util.Date;

public class ChatHistoryEntity {
    private int id;
    private int hid;
    private String message;
    private String role;
    private Date timestamp;
    private int uid; // Add uid field

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getUid() { // Add getter
        return uid;
    }

    public void setUid(int uid) { // Add setter
        this.uid = uid;
    }
}