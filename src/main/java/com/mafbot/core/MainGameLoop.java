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

    private List<User> users;
    private OutgoingSender outgoingSender;

    public MainGameLoop(OutgoingSender outgoingSender) {
        this.outgoingSender = outgoingSender;
        users = new ArrayList<>();
    }

    /**
     * Добавляет пользователя в список играющих, если его там еще нет.
     * @param user
     * @return номер, под которым игрок был зарегистрирован.
     */
    public int addUser(User user) {
        if (!isUserInGame(user)) {
            users.add(user);
            return users.size() + 1;
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getChatIdPerson().compareTo(user.getChatIdPerson()) == 0) {
                return i + 1;
            }
        }
        throw new RuntimeException(String.format("Пользователя с id = '%s' нет в списке %s, но этого быть не должно!",
                user.getChatIdPerson(), users));
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean isUserInGame(User user) {
        return users.contains(user);
    }

    @Override
    public void run() {
        int gameNumber = BeanRepository.getInstance().getStatisticsService().getGameNumber();
        log.info("Стартует игра №{}", gameNumber);
        log.info("Начинается регистрация в игру №{}", gameNumber);
        registrationPhase();
        log.info("Окончена регистрация в игру №{}", gameNumber);
        log.info("Начинается геренация и раздача ролей в игре №{}", gameNumber);
        generateRoles();
        log.info("Окончена генерация и раздача ролей в игре №{}", gameNumber);
        log.info("Начинается основной цикл ночь-день в игре №{}", gameNumber);
        nightDayPhase();
        log.info("Прерван основной цикл ночь-день в игре №{}", gameNumber);
        finishGame();
        log.info("Окончена игра №{}", gameNumber);
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
        outgoingSender.sendInCommonChannel("Здесь будут генерироваться роли и раздаваться зарегистрированным игрокам");
    }

    private void nightDayPhase() {
        outgoingSender.sendInCommonChannel("Здесь будет цикл ночь-день");
    }

    private void finishGame() {
        outgoingSender.sendInCommonChannel("Это окончание игры");
    }
}
