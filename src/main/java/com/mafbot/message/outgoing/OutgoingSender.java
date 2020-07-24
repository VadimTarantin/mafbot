package com.mafbot.message.outgoing;

import com.mafbot.user.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

public interface OutgoingSender {
    void setSender(AbsSender sender);
    void sendDirectly(Long chatIdPerson, String message);
    void sendInCommonChannel(String message);
    void sendToUsersRoleSpecificText(List<User> users, Integer maxNumberPotentialTarget);
    void sendToUsersCommonText(List<User> users, String text);
}