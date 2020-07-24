package com.mafbot;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.initialization.MafTelegramBotPropertiesHolder;
import com.mafbot.message.incoming.model.IncomingData;
import com.mafbot.message.incoming.IncomingMessageManager;
import com.mafbot.message.outgoing.OutgoingSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MafTelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LogManager.getLogger(MafTelegramBot.class);

    private OutgoingSender outgoingSender;
    private IncomingMessageManager incomingMessageManager;

    public MafTelegramBot() {
        outgoingSender = BeanRepository.getInstance().getOutgoingSender();
        outgoingSender.setSender(this);
        incomingMessageManager = BeanRepository.getInstance().getIncomingMessageManager();
    }

    @Override
    public String getBotUsername() {
        return MafTelegramBotPropertiesHolder.getInstance().getBotName();
    }

    @Override
    public String getBotToken() {
        return MafTelegramBotPropertiesHolder.getInstance().getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message incomingMessage = update.getMessage();
        Long chatId = incomingMessage.getChatId();
        String incomingMessageText = incomingMessage.getText();
        String userName = incomingMessage.getFrom().getUserName();

        IncomingData incomingData = new IncomingData(chatId, userName, incomingMessageText);
        log.debug("Получено сообщение от пользователя с id='{}' и userName='{}': '{}'",
                incomingData.getChatId(), incomingData.getUserName(), incomingData.getMessage());

        incomingMessageManager.handleIncomingMessage(incomingData);
    }
}