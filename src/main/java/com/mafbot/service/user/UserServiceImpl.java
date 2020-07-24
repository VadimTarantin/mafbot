package com.mafbot.service.user;

import com.mafbot.message.incoming.model.IncomingData;
import com.mafbot.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private Map<Long, User> activeUsers = new HashMap<>();

    @Override
    public User getUser(IncomingData incomingData) {
        Long userChatId = incomingData.getChatId();
        if (activeUsers.containsKey(userChatId)) {
            User result = activeUsers.get(userChatId);
            log.info("Пользователь {} c chatId={} взят из кэша", result.getName(), result.getChatIdPerson());
            return result;
        }

        User result = new User(userChatId, incomingData.getUserName());
        activeUsers.put(userChatId, result);
        log.info("Пользователь {} с chatId={} положен в кэш", result.getName(), result.getChatIdPerson());
        return result;
    }

    public void cleanInactiveUsers() {
        //не забыть сделать очистку кэша от неактивных пользователей
    }
}
