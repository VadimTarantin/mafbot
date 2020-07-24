package com.mafbot.message.incoming;

import com.mafbot.message.outgoing.OutgoingSender;

public interface IncomingMessageManager {
    void setOutgoingSender(OutgoingSender outgoingSender);
    void handleIncomingMessage(Long chatId, String incomingMessage);
}
