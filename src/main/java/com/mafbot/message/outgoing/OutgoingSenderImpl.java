package com.mafbot.message.outgoing;

import com.mafbot.initialization.MafTelegramBotPropertiesHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class OutgoingSenderImpl implements OutgoingSender {
    private static final Logger log = LogManager.getLogger(OutgoingSenderImpl.class);

    private AbsSender sender;

    @Override
    public void setSender(AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendDirectly(Long chatIdPerson, String message) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatIdPerson);
        answer.setText(message);

        try {
            sender.execute(answer);
            log.debug("Успешно отправлено сообщение пользователю с id='{}': '{}'", answer.getChatId(), answer.getText());
        } catch (TelegramApiException e) {
            log.warn("Не удалось отправить сообщение пользователю с id='{}': '{}'", answer.getChatId(), answer.getText(), e);
        }
    }

    @Override
    public void sendInCommonChannel(String message) {
        SendMessage answer = new SendMessage();
        answer.setChatId(MafTelegramBotPropertiesHolder.getInstance().getChannelIdCommon());
        answer.setText(message);

        try {
            sender.execute(answer);
            log.debug("Успешно отправлено сообщение в общий канал с id='{}': '{}'", answer.getChatId(), answer.getText());
        } catch (TelegramApiException e) {
            log.warn("Не удалось отправить сообщение в общий канал с id='{}': '{}'", answer.getChatId(), answer.getText(), e);
        }
    }
}