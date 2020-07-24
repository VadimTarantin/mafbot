package com.mafbot.message.incoming;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.initialization.MafTelegramBotPropertiesHolder;
import com.mafbot.message.incoming.model.IncomingData;
import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IncomingMessageManagerImpl implements IncomingMessageManager {
    private static final Logger log = LogManager.getLogger(IncomingMessageManagerImpl.class);

    private OutgoingSender outgoingSender;

    @Override
    public void setOutgoingSender(OutgoingSender outgoingSender) {
        this.outgoingSender = outgoingSender;
    }

    @Override
    public void handleIncomingMessage(IncomingData incomingData) {
        if (shouldBeProcessed(incomingData)) {
            handleIncomingMsg(incomingData);
        }
    }

    private boolean shouldBeProcessed(IncomingData incomingData) {
        if (MafTelegramBotPropertiesHolder.getInstance().getChannelIdCommon().equalsIgnoreCase(incomingData.getChatId().toString())) {
            //сообщение из общего чата
            return false;
        }
        if (Command.isCommandAvailable(incomingData.getMessage())) {
            return true;
        }
        return false;
    }

    private void handleIncomingMsg(IncomingData incomingData) {
        String incomingMessage = incomingData.getMessage();
        Long chatId = incomingData.getChatId();

        User currentUser = BeanRepository.getInstance().getUserService().getUser(incomingData);

        if (Command.REG.name.equalsIgnoreCase(incomingMessage) || Command.REG_ALT.name.equalsIgnoreCase(incomingMessage)) {
            BeanRepository.getInstance().getGameManager().registrationInCurrentGame(currentUser);
        } else if (Command.START.name.equalsIgnoreCase(incomingMessage) || Command.START_ALT.name.equalsIgnoreCase(incomingMessage)) {
            BeanRepository.getInstance().getGameManager().startGame(currentUser);
        } else if (Command.ROLE.name.equalsIgnoreCase(incomingMessage) || Command.ROLE_ALT.name.equalsIgnoreCase(incomingMessage)) {
            String answer = String.format("%s, ваша роль - %s!", currentUser.getName(), currentUser.getRole().getName());
            outgoingSender.sendDirectly(chatId, answer);
        } else if (Command.LIST.name.equalsIgnoreCase(incomingMessage) || Command.LIST_ALT.name.equalsIgnoreCase(incomingMessage)) {
            BeanRepository.getInstance().getGameManager().sendPlayersList(chatId);
        }
    }
}