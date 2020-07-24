package com.mafbot.core;

import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DayNightLoop {
    private static final Logger log = LogManager.getLogger(DayNightLoop.class);

    private List<User> usersAliveInCurrentGame; //игроки, которые сейчас живы в игре
    private OutgoingSender outgoingSender;

    private int nightNumber;

    public DayNightLoop(List<User> usersAliveInCurrentGame, OutgoingSender outgoingSender) {
        this.usersAliveInCurrentGame = usersAliveInCurrentGame;
        this.outgoingSender = outgoingSender;
    }

    void doLoop() {
        while (true) {
            try {
                doNight();
                if (isVictory()) {
                    break;
                }
                doDay();
                if (isVictory()) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isVictory() {
        return usersAliveInCurrentGame.size() <= 1;
    }

    private void doNight() throws InterruptedException {
        outgoingSender.sendInCommonChannel(String.format("Наступает %s ночь. Город засыпает", ++nightNumber));
        Thread.sleep(2000);
        int size = usersAliveInCurrentGame.size();
        int randomNumber = (int) (Math.random() * size);
        User user = usersAliveInCurrentGame.get(randomNumber);
        String msg = String.format("Эта реализация ночи просто убивает одного случайного игрока. В этот раз не повезло %s %s!",
                user.getRole(), user.getName());
        outgoingSender.sendInCommonChannel(msg);
        killUser(user);

        Thread.sleep(5000L);
        outgoingSender.sendInCommonChannel(String.format("Ночь %s окончена и город просыпается", nightNumber));
    }

    private void doDay() {
        outgoingSender.sendInCommonChannel("Дня пока не будет!");
    }

    private void killUser(User user) {
        log.info("Умирает {} {}", user.getRole(), user.getName());
        usersAliveInCurrentGame.remove(user);

    }
}