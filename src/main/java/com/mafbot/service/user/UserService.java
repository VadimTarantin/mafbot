package com.mafbot.service.user;

import com.mafbot.message.incoming.model.IncomingData;
import com.mafbot.user.User;

public interface UserService {
    /**
     * Возвращается ассоциированного пользователя бота.
     * @param incomingData
     * @return
     */
    User getUser(IncomingData incomingData);
}