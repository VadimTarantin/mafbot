package com.mafbot.message.incoming.model;

public class IncomingData {
    private final Long chatId;
    private final String userName;
    private final String message;

    public IncomingData(Long chatId, String userName, String message) {
        this.chatId = chatId;
        this.userName = userName;
        this.message = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
}
