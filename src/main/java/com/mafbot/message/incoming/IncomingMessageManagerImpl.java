package com.mafbot.message.incoming;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.incoming.model.IncomingData;
import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.user.User;

public class IncomingMessageManagerImpl implements IncomingMessageManager {

    private OutgoingSender outgoingSender;

    @Override
    public void setOutgoingSender(OutgoingSender outgoingSender) {
        this.outgoingSender = outgoingSender;
    }

    @Override
    public void handleIncomingMessage(IncomingData incomingData) {
        String incomingMessage = incomingData.getMessage();
        Long chatId = incomingData.getChatId();

        User currentUser = BeanRepository.getInstance().getUserService().getUser(incomingData);

        if ("/рег".equalsIgnoreCase(incomingMessage) || "/reg".equalsIgnoreCase(incomingMessage)) {
            BeanRepository.getInstance().getGameManager().registrationInCurrentGame(currentUser);
        } else if ("/старт".equalsIgnoreCase(incomingMessage) || "/start".equalsIgnoreCase(incomingMessage)) {
            BeanRepository.getInstance().getGameManager().startGame(currentUser);
        } else if ("/регми".equalsIgnoreCase(incomingMessage) || "/regme".equalsIgnoreCase(incomingMessage)) {
            String answer = "Здесь будет регистрация в боте";
            outgoingSender.sendDirectly(chatId, answer);
        } else if ("/роль".equalsIgnoreCase(incomingMessage) || "/role".equalsIgnoreCase(incomingMessage)) {
            String answer = String.format("%s, ваша роль - %s!", currentUser.getName(), currentUser.getRole().getName());
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