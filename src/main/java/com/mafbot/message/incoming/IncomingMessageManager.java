package com.mafbot.message.incoming;

import com.mafbot.message.incoming.model.IncomingData;
import com.mafbot.message.outgoing.OutgoingSender;

public interface IncomingMessageManager {
    void setOutgoingSender(OutgoingSender outgoingSender);
    void handleIncomingMessage(IncomingData incomingData);
}
