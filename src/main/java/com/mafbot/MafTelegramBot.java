package com.mafbot;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.initialization.MafTelegramBotPropertiesHolder;
import com.mafbot.message.outgoing.OutgoingSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MafTelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LogManager.getLogger(MafTelegramBot.class);

    private OutgoingSender outgoingSender;

    public MafTelegramBot() {
        outgoingSender = BeanRepository.getInstance().getOutgoingSender();
        outgoingSender.setSender(this);
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
        log.info("Получено сообщение от пользователя с id='{}': '{}'", chatId, incomingMessageText);

        String message = "Ваш id: '" + chatId +"', ваше сообщение: '" + incomingMessageText + "'";

        outgoingSender.sendDirectly(chatId, message);
        sendMessageInCommonChannel(chatId, incomingMessage.getText());
    }

    private void sendMessageInCommonChannel(Long chatIdPerson, String incomingFromPersonMessage) {
        String forCommonAnswer = "От пользователя с id = '" + chatIdPerson + "' получено сообщение '"
                + incomingFromPersonMessage + "'!";
        outgoingSender.sendInCommonChannel(forCommonAnswer);
    }

}