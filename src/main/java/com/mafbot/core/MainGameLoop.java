package com.mafbot.core;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainGameLoop extends Thread {
    private static final Logger log = LogManager.getLogger(MainGameLoop.class);

    private static final long REGISTRATION_TIME_AMOUNT = 1 * 60 * 1000;

    private List<User> usersInStartGame;
    private List<User> usersAliveInCurrentGame;
    private OutgoingSender outgoingSender;

    public MainGameLoop(OutgoingSender outgoingSender) {
        this.outgoingSender = outgoingSender;
        usersInStartGame = new ArrayList<>();
        usersAliveInCurrentGame = new ArrayList<>();
    }

    /**
     * Добавляет пользователя в список играющих, если его там еще нет.
     * @param user
     * @return номер, под которым игрок зарегистрирован в игре.
     */
    public int addUser(User user) {
        if (!isUserInGame(user)) {
            usersAliveInCurrentGame.add(user);
            return usersAliveInCurrentGame.size() + 1;
        }
        for (int i = 0; i < usersAliveInCurrentGame.size(); i++) {
            if (usersAliveInCurrentGame.get(i).getChatIdPerson().compareTo(user.getChatIdPerson()) == 0) {
                return i + 1;
            }
        }
        throw new RuntimeException(String.format("Пользователя с id = '%s' нет в списке %s, но этого быть не должно!",
                user.getChatIdPerson(), usersAliveInCurrentGame));
    }

    public boolean isUserInGame(User user) {
        return usersAliveInCurrentGame.contains(user);
    }

    @Override
    public void run() {
        int gameNumber = BeanRepository.getInstance().getStatisticsService().getGameNumber();
        log.info("Стартует игра №{}", gameNumber);
        log.info("Начинается регистрация в игру №{}", gameNumber);
        registrationPhase();
        log.info("Окончена регистрация в игру №{}", gameNumber);
        log.info("Начинается генерация и раздача ролей в игре №{}", gameNumber);
        generateRoles();
        log.info("Окончена генерация и раздача ролей в игре №{}", gameNumber);
        log.info("Начинается основной цикл ночь-день в игре №{}", gameNumber);
        nightDayPhase();
        log.info("Прерван основной цикл ночь-день в игре №{}", gameNumber);
        finishGame();
        log.info("Окончена игра №{}", gameNumber);
        postFinishAction();
    }

    private void registrationPhase() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(REGISTRATION_TIME_AMOUNT);
                interrupt();
            } catch (InterruptedException e) {
                log.warn("Неожиданное прерывание фазы регистрации в игру номер {}",
                        BeanRepository.getInstance().getStatisticsService().getGameNumber(), e);
            }
        }
        String answer = String.format("Регистрация в игру №%s завершена!",
                BeanRepository.getInstance().getStatisticsService().getGameNumber());
        outgoingSender.sendInCommonChannel(answer);
    }

    private void generateRoles() {
        BeanRepository.getInstance().getGeneratorRoleService().generateRoles(usersAliveInCurrentGame);
        usersInStartGame = new ArrayList<>(usersAliveInCurrentGame);
    }

    private void nightDayPhase() {
        outgoingSender.sendInCommonChannel("Здесь будет цикл ночь-день");
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outgoingSender.sendInCommonChannel("Цикл ночь-день окончен!");
    }

    private void finishGame() {
        int gameNumber = BeanRepository.getInstance().getStatisticsService().getGameNumber();
        String answer = String.format("Игра №%s завершена! Участвовали: %s", gameNumber, getUsersListInGame());
        outgoingSender.sendInCommonChannel(answer);
    }

    private void postFinishAction() {
        int gameNumber = BeanRepository.getInstance().getStatisticsService().getGameNumber();
        for (User user : usersInStartGame) {
            user.setRole(null);
        }

        for (User user : usersAliveInCurrentGame) {
            user.setRole(null);
        }

        usersAliveInCurrentGame.clear();
        usersInStartGame.clear();
        log.info("После игры №{} очищены списки игроков. У игроков обнулены роли", gameNumber);
    }

    /**
     * @return "В игре: 1: firstName, 2: secondName"
     */
    public String getUsersListInGame() {
        StringBuilder result = new StringBuilder();
        if (usersAliveInCurrentGame.isEmpty()) {
            result.append("В игре никого нет!");
            return result.toString();
        }
        result.append("В игре: ");
        for (int i = 0; i < usersAliveInCurrentGame.size(); i++) {
            result.append(i + 1).append(": ").append(usersAliveInCurrentGame.get(i).getName()).append(", ");
        }

        if (result.length() > 2) {
            result.delete(result.length() - 2, result.length());
        }
        return result.toString();
    }
}
