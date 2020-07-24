package com.mafbot.message.incoming;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.outgoing.OutgoingSender;

public class IncomingMessageManagerImpl implements IncomingMessageManager {

    private OutgoingSender outgoingSender;

    @Override
    public void setOutgoingSender(OutgoingSender outgoingSender) {
        this.outgoingSender = outgoingSender;
    }

    @Override
    public void handleIncomingMessage(Long chatId, String incomingMessage) {
        if ("/рег".equalsIgnoreCase(incomingMessage) || "/reg".equalsIgnoreCase(incomingMessage)) {
            BeanRepository.getInstance().getGameManager().startGame(chatId);
        } else if ("/регми".equalsIgnoreCase(incomingMessage) || "/regme".equalsIgnoreCase(incomingMessage)) {
            String answer = "Здесь будет регистрация в боте";
            outgoingSender.sendDirectly(chatId, answer);
        } else if ("/лист".equalsIgnoreCase(incomingMessage) || "/list".equalsIgnoreCase(incomingMessage)) {
            BeanRepository.getInstance().getGameManager().sendGamersList(chatId);
        } else {
            String answer = "Ваш id: '" + chatId + "', ваше сообщение: '" + incomingMessage + "'";
            outgoingSender.sendDirectly(chatId, answer);
            outgoingSender.sendInCommonChannel(answer);
        }
    }
}