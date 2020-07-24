package com.mafbot;

import com.mafbot.core.LoopGameSpy;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class MafBotApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MafTelegramBot());
            new Thread(new LoopGameSpy()).start();
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}