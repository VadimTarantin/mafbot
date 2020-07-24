package com.mafbot.core;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.user.User;

public class GameManager {
    private boolean gameIsStarted;
    private MainGameLoop mainGameLoop;
    private OutgoingSender outgoingSender;

    public void setOutgoingSender(OutgoingSender outgoingSender) {
        this.outgoingSender = outgoingSender;
    }

    public synchronized void startGame(Long chatId) {
        if (!gameIsStarted) {
            mainGameLoop = new MainGameLoop(outgoingSender);
            mainGameLoop.addUser(new User(chatId));
            gameIsStarted = true;
            mainGameLoop.start();

            int gameNumber = BeanRepository.getInstance().getStatisticsService().getNewGameNumber();
            String answerInDirect = String.format("Вы запустили регистрацию в новую игру №%s! Вы успешно зарегистрировались под номером 1!",
                    gameNumber);
            outgoingSender.sendDirectly(chatId, answerInDirect);
            String answerInCommon = String.format("Объявляется регистрация в новую игру №%s!", gameNumber);
            outgoingSender.sendInCommonChannel(answerInCommon);
        } else {
            int userNumberInGame = mainGameLoop.addUser(new User(chatId));
            String answer = String.format("Вы успешно зарегистрировались в игру №%s под номером %s!",
                    BeanRepository.getInstance().getStatisticsService().getGameNumber(), userNumberInGame);
            outgoingSender.sendDirectly(chatId, answer);
        }
    }

    public synchronized void finishGame() {
        gameIsStarted = false;
        mainGameLoop.interrupt();
        mainGameLoop = null;
    }

    public void sendGamersList(Long chatId) {
        if (gameIsStarted) {
            String answer = String.format("В игре №%s: %s",
                    BeanRepository.getInstance().getStatisticsService().getGameNumber(), mainGameLoop.getUsers());
            outgoingSender.sendDirectly(chatId, answer);
        } else {
            String answer = "В игре никого нет! Вы можете запустить новую игру командой /рег";
            outgoingSender.sendDirectly(chatId, answer);
        }

    }
}
