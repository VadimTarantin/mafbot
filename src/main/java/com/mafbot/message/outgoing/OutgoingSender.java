package com.mafbot.message.outgoing;

import org.telegram.telegrambots.meta.bots.AbsSender;

public interface OutgoingSender {
    void setSender(AbsSender sender);
    void sendDirectly(Long chatIdPerson, String message);
    void sendInCommonChannel(String message);
}