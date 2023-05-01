package es.MiHipotecaApp.TFG.Transfers;

import java.util.Date;

public class Message {
    private String text;
    private String sender;
    private Date sentAt;

    public Message() {}

    public Message(String text, String sender, Date sentAt) {
        this.text = text;
        this.sender = sender;
        this.sentAt = sentAt;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public Date getSentAt() {
        return sentAt;
    }
}
