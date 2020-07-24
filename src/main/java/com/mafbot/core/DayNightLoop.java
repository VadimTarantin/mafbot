package com.mafbot.core;

import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.role.support.ProcessResult;
import com.mafbot.role.support.Target;
import com.mafbot.service.role.RoleService;
import com.mafbot.user.Order;
import com.mafbot.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DayNightLoop {
    private static final Logger log = LogManager.getLogger(DayNightLoop.class);

    private List<User> usersAliveInCurrentGame; //игроки, которые сейчас живы в игре
    private OutgoingSender outgoingSender;
    private MainGameLoop mainGameLoop;
    private RoleService roleService;

    private int nightNumber;

    public DayNightLoop(List<User> usersAliveInCurrentGame, OutgoingSender outgoingSender, MainGameLoop mainGameLoop,
                        RoleService roleService) {
        this.usersAliveInCurrentGame = usersAliveInCurrentGame;
        this.outgoingSender = outgoingSender;
        this.mainGameLoop = mainGameLoop;
        this.roleService = roleService;
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
        sendMessageAboutOrdersToPlayers();

        for (int i = 0; i < 4; i++) {
            Thread.sleep(25000L);
            if (areAllPlayersDoneOrders()) {
                break;
            }
            outgoingSender.sendInCommonChannel("Роли, не спать! Пора делать свой заказ!");
        }

        Thread.sleep(2000L);
        outgoingSender.sendInCommonChannel(String.format("Ночь %s окончена и город просыпается", nightNumber));
    }

    private void doDay() throws InterruptedException {
        outgoingSender.sendInCommonChannel("Наступает день, который покажет, кто кого недосчитался!");
        processOrders();
        outgoingSender.sendInCommonChannel("Здесь будет голосование за повешение одного из игроков");
    }

    private void killUser(User user) {
        log.info("Умирает {} {}", user.getRole(), user.getName());
        usersAliveInCurrentGame.remove(user);

    }

    private void sendMessageAboutOrdersToPlayers() {
        String usersListInGame = mainGameLoop.getUsersListInGame();
        List<User> usersWithActiveRoles = roleService.getUsersWithActiveRoles(usersAliveInCurrentGame);

        //рассылаем каждому игроку сообщения о необходимости сделать заказ
        outgoingSender.sendToUsersRoleSpecificText(usersWithActiveRoles, usersAliveInCurrentGame.size());
        outgoingSender.sendToUsersCommonText(usersWithActiveRoles, usersListInGame);
    }

    private boolean areAllPlayersDoneOrders() {
        List<User> usersWithActiveRoles = roleService.getUsersWithActiveRoles(usersAliveInCurrentGame);
        for (User usersWithActiveRole : usersWithActiveRoles) {
            if (!usersWithActiveRole.getOrder().wasOrder()) {
                return false;
            }
        }
        return true;
    }

    private void processOrders() throws InterruptedException {
        List<User> diedPlayers = new ArrayList<>();

        List<User> usersWithActiveRoles = roleService.getUsersWithActiveRoles(usersAliveInCurrentGame);
        for (User user : usersWithActiveRoles) {
            Order order = user.getOrder();
            if (order.wasOrder()) {
                Integer target = order.getTarget();
                User targetUser = usersWithActiveRoles.get(target - 1);
                ProcessResult result = user.getRole().doActionTo(new Target(targetUser.getRole(),targetUser));
                outgoingSender.sendInCommonChannel(result.getMessageAboutResultForCommonChannel());
                if (result.existMessageAboutResultForOrdered()) {
                    outgoingSender.sendDirectly(user.getChatIdPerson(), result.getMessageAboutResultForOrdered());
                }

                if (result.isTargetIsDied()) {
                    log.debug("Игрок {} с ролью {} умирает от действий {}",
                            targetUser.getName(),targetUser.getRole(), user.getName());
                    diedPlayers.add(targetUser);
                }
            } else {
                outgoingSender.sendInCommonChannel(user.getRole().getSkipTurnTest());
            }

            user.setOrder(null);
            Thread.sleep(2000L);
        }

        usersAliveInCurrentGame.removeAll(diedPlayers);
    }
}