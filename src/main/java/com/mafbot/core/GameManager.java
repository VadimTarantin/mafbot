package com.mafbot.core;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameManager {
    private static final Logger log = LogManager.getLogger(GameManager.class);

    private MainGameLoop mainGameLoop;
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
    }

    public void sendPlayersList(Long chatId) {
        if (mainGameLoop == null || GameStatus.AWAITING == mainGameLoop.getGameStatus()) {
            String answer = "В игре никого нет! Вы можете запустить новую игру командой /start";
            outgoingSender.sendDirectly(chatId, answer);
        } else if (GameStatus.AWAITING != mainGameLoop.getGameStatus()) {
            String answer = String.format("В игре №%s: %s",
                    BeanRepository.getInstance().getStatisticsService().getGameNumber(),
                    mainGameLoop.getUsersListInGame());
            outgoingSender.sendDirectly(chatId, answer);
        }
    }

    /**
     * Только стартует новую игру. Пользователь не регистритуется в ней.
     */
    private void startNewGame(User user) {
        if (gameCanBeStarted()) {
            //стартуем
            mainGameLoop = new MainGameLoop(outgoingSender);
            int gameNumber = BeanRepository.getInstance().getStatisticsService().getNewGameNumber();
            mainGameLoop.setGameStatus(GameStatus.REGISTRATION);
            mainGameLoop.start();

            String answerInDirect = String.format("Вы запустили регистрацию в новую игру №%s!",
                    gameNumber);
            outgoingSender.sendDirectly(user.getChatIdPerson(), answerInDirect);
            String answerInCommon = String.format("Объявляется регистрация в новую игру №%s!", gameNumber);
            outgoingSender.sendInCommonChannel(answerInCommon);
        } else {
            String answer = String.format("Невозможно стартануть новую игру. Текущая игра в статусе '%s'", mainGameLoop.getGameStatus());
            outgoingSender.sendDirectly(user.getChatIdPerson(), answer);
        }
    }

    /**
     * Регистрирует пользователя в игре, если игра в режиме регистрации.
     * @param user пользователь в боте.
     */
    public void registrationInCurrentGame(User user) {
        if (isOpenRegistration()) {
            if (mainGameLoop.isUserInGame(user)) {
                String answer = String.format("Вы уже в игре под номером %s!", mainGameLoop.getPlayerNumber(user));
                outgoingSender.sendDirectly(user.getChatIdPerson(), answer);
            } else {
                int userNumberInGame = mainGameLoop.addUser(user);
                int gameNumber = BeanRepository.getInstance().getStatisticsService().getGameNumber();
                String answer = String.format("Вы успешно зарегистрировались в игре №'%s' под номером %s!", gameNumber, userNumberInGame);
                outgoingSender.sendDirectly(user.getChatIdPerson(), answer);
            }
        } else {
            String answer;
            if (mainGameLoop == null) {
                answer = String.format("Невозможно зарегистрировать вас в игру. Текущая игра в статусе '%s'", GameStatus.AWAITING);
            } else {
                answer = String.format("Невозможно зарегистрировать вас в игру. Текущая игра в статусе '%s'", mainGameLoop.getGameStatus());
            }
            outgoingSender.sendDirectly(user.getChatIdPerson(), answer);
        }
    }

    private boolean gameCanBeStarted() {
        return mainGameLoop == null || GameStatus.AWAITING == mainGameLoop.getGameStatus();
    }

    private boolean isOpenRegistration() {
        if (mainGameLoop == null) {
            return false;
        }
        if (GameStatus.REGISTRATION == mainGameLoop.getGameStatus()) {
            return true;
        }
        return false;
    }

    public void processDigitCommand(User currentUser, Integer digit) {
        if (mainGameLoop != null ) {
            mainGameLoop.processGameInProcessCommand(currentUser, digit);
        }
    }
}
