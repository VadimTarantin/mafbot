package com.mafbot.core;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.user.User;

public class GameManager {
    private MainGameLoop mainGameLoop;
    private GameStatus gameStatus;
    private OutgoingSender outgoingSender;

    public void setOutgoingSender(OutgoingSender outgoingSender) {
        this.outgoingSender = outgoingSender;
    }

    public synchronized void startGame(User user) {
        startNewGame(user);
        registrationInCurrentGame(user);
    }

    public synchronized void finishGame() {
        mainGameLoop.interrupt();
        mainGameLoop = null;
        gameStatus = GameStatus.AWAITING;
    }

    public void sendGamersList(Long chatId) {
        if (gameStatus != GameStatus.AWAITING) {
            String answer = String.format("В игре №%s: %s",
                    BeanRepository.getInstance().getStatisticsService().getGameNumber(),
                    mainGameLoop.getUsersListInGame());
            outgoingSender.sendDirectly(chatId, answer);
        } else {
            String answer = "В игре никого нет! Вы можете запустить новую игру командой /рег";
            outgoingSender.sendDirectly(chatId, answer);
        }
    }

    /**
     * Только стартует новую игру. Пользователь не регистритуется в ней.
     */
    private void startNewGame(User user) {
        if (GameStatus.AWAITING == gameStatus) {
            //стартуем
            mainGameLoop = new MainGameLoop(outgoingSender);
            mainGameLoop.addUser(user);
            mainGameLoop.start();

            int gameNumber = BeanRepository.getInstance().getStatisticsService().getNewGameNumber();
            String answerInDirect = String.format("Вы запустили регистрацию в новую игру №%s!",
                    gameNumber);
            outgoingSender.sendDirectly(user.getChatIdPerson(), answerInDirect);
            String answerInCommon = String.format("Объявляется регистрация в новую игру №%s!", gameNumber);
            outgoingSender.sendInCommonChannel(answerInCommon);
        } else {
            String answer = String.format("Невозможно стартануть новую игру. Текущая игра в статусе '%s'", gameStatus);
            outgoingSender.sendInCommonChannel(answer);
        }
    }

    /**
     * Регистрирует пользователя в игре, если игра в режиме регистрации.
     * @param user пользователь в боте.
     */
    public void registrationInCurrentGame(User user) {
        if (GameStatus.REGISTRATION == gameStatus) {
            int userNumberInGame = mainGameLoop.addUser(user);
            if (mainGameLoop.isUserInGame(user)) {
                String answer = String.format("Вы уже в игре под номером %s!", userNumberInGame);
                outgoingSender.sendInCommonChannel(answer);
            } else {
                int gameNumber = BeanRepository.getInstance().getStatisticsService().getNewGameNumber();
                String answer = String.format("Вы успешно зарегистрировались в игре №'%s' под номером %s!", gameNumber, userNumberInGame);
                outgoingSender.sendInCommonChannel(answer);
            }
        } else {
            String answer = String.format("Невозможно зарегистрировать вас в игру. Текущая игра в статусе '%s'", gameStatus);
            outgoingSender.sendInCommonChannel(answer);
        }
    }
}
