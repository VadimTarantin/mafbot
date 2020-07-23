package com.mafbot;

import com.mafbot.initialization.MafTelegramBotPropertiesHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MafTelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LogManager.getLogger(MafTelegramBot.class);

    private static final long BLT_MAFIA_CHANNEL_CHAT_ID = -1001359253588L;
    private static final long MAFBOT_CHANNEL_CHAT_ID = -1001256422830L;

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
        String text = incomingMessage.getText();
        log.info("Получено сообщение от пользователя с id='{}': '{}'", chatId, text);

        SendMessage botAnswer = new SendMessage();
        botAnswer.setChatId(chatId);
        botAnswer.setText("Ваш id: '" + chatId +"', ваше сообщение: '" + text + "'");

        try {
            execute(botAnswer);
            sendMessageInCommonChannel(chatId, text);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void sendMessageInCommonChannel(Long chatIdPerson, String incomingFromPersonMessage) throws TelegramApiException {
        /*SendMessage inBLTAnswer = new SendMessage();
        inBLTAnswer.setChatId(BLT_MAFIA_CHANNEL_CHAT_ID);
        inBLTAnswer.setText("От пользователя с id = '" + chatIdPerson + "' получено сообщение '"
                + incomingFromPersonMessage + "'!");

        execute(inBLTAnswer);*/

        /*SendMessage inMafBotAnswer = new SendMessage();
        inMafBotAnswer.setChatId(MAFBOT_CHANNEL_CHAT_ID);
        inMafBotAnswer.setText("От пользователя с id = '" + chatIdPerson + "' получено сообщение '"
                + incomingFromPersonMessage + "'!");

        execute(inMafBotAnswer);*/

        SendMessage answer = new SendMessage();
        answer.setChatId(MafTelegramBotPropertiesHolder.getInstance().getChannelIdCommon());
        answer.setText("От пользователя с id = '" + chatIdPerson + "' получено сообщение '"
                + incomingFromPersonMessage + "'!");

        execute(answer);

        log.info("Успешно отправлено сообщение пользователю с id='{}': '{}'", answer.getChatId(), answer.getText());
    }

}