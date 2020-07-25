package com.mafbot.core;

import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.role.support.ProcessResult;
import com.mafbot.role.support.Target;
import com.mafbot.service.role.RoleService;
import com.mafbot.user.Order;
import com.mafbot.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class DayNightLoop {
    private static final Logger log = LogManager.getLogger(DayNightLoop.class);

    private List<User> usersAliveInCurrentGame; //игроки, которые сейчас живы в игре
    private OutgoingSender outgoingSender;
    private MainGameLoop mainGameLoop;
    private RoleService roleService;

    private DayNightStatus dayNightStatus = DayNightStatus.UNKNOWN;
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
                mainGameLoop.setDayNightStatus(DayNightStatus.NIGHT);
                doNight();
                if (isVictory()) {
                    break;
                }
                mainGameLoop.setDayNightStatus(DayNightStatus.DAY);
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
        outgoingSender.sendInCommonChannel(mainGameLoop.getUsersListInGame());
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
        sendMessageAboutDayVoting();
        for (int i = 0; i < 4; i++) {
            Thread.sleep(25000L);
            if (areAllPlayersDoneVote()) {
                break;
            }
            outgoingSender.sendInCommonChannel("Не забудьте выбрать, кого пора обнулить!");
        }
        processDayVoting();
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

    private boolean areAllPlayersDoneVote() {
        for (User user : usersAliveInCurrentGame) {
            if (!user.getOrder().wasOrder()) {
                return false;
            }
        }
        log.debug("Определено, что все проголосовали днем!");
        return true;
    }

    private void processOrders() throws InterruptedException {
        List<User> diedPlayers = new ArrayList<>();

        List<User> usersWithActiveRoles = roleService.getUsersWithActiveRoles(usersAliveInCurrentGame);
        for (User user : usersWithActiveRoles) {
            Order order = user.getOrder();
            if (order.wasOrder()) {
                Integer target = order.getTarget();
                User targetUser = usersAliveInCurrentGame.get(target - 1);
                ProcessResult result = user.getRole().doActionTo(new Target(targetUser.getRole(), targetUser.getName()));
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

    private void sendMessageAboutDayVoting() {
        outgoingSender.sendInCommonChannel("Кто самое слабое звено? Выберите номер того, кого следует убить!");
        outgoingSender.sendToUsersCommonText(usersAliveInCurrentGame, "Пора голововать!");
        outgoingSender.sendToUsersAboutDayVotingText(usersAliveInCurrentGame, usersAliveInCurrentGame.size(), mainGameLoop.getUsersListInGame());
    }

    private void processDayVoting() {
        //обработка дневного голосования
        Map<Integer, Integer> votingResult = new HashMap<>(); //номер игрока из списка живых, количество голосов за него

        for (User user : usersAliveInCurrentGame) {
            Order order = user.getOrder();
            if (order.wasOrder()) {
                Integer target = order.getTarget(); //номер игрока цели
                log.debug("Товарищ {} голосует за {}", user.getName(), target);
                if (votingResult.containsKey(target)) {
                    Integer lastAmount = votingResult.get(target);
                    votingResult.put(target, lastAmount + 1);
                } else {
                    votingResult.put(target, 1);
                }
            }
            user.setOrder(null);
        }
        log.debug("Результаты голосования: {}", votingResult);

        //определение, за кого большинство и его убийство
        Collection<Integer> votes = votingResult.values();
        Set<Integer> unique = new HashSet<>(votes);
        if (votes.size() != unique.size()) {
            outgoingSender.sendInCommonChannel("Жители не смогли договориться о жертве!");
            return;
        }

        //кого-то выбрали
        int numberPlayerForKill = 0;
        int amountVote = 0;
        for (Integer playerNumber : votingResult.keySet()) {
            Integer amountVoteForCurrentUser = votingResult.get(playerNumber);
            if (amountVoteForCurrentUser > amountVote) {
                amountVote = amountVoteForCurrentUser;
                numberPlayerForKill = playerNumber;
            }
        }

        log.debug("Определен номер игрока для повешения: {} (-1). Список игроков: {}",
                numberPlayerForKill, usersAliveInCurrentGame);
        User userForKull = usersAliveInCurrentGame.get(numberPlayerForKill - 1);

        String message = String.format("Жители решили, что во всем виноват %s %s и повесили его!",
                userForKull.getRole().getName(), userForKull.getName());
        outgoingSender.sendInCommonChannel(message);
        killUser(userForKull);
    }
}