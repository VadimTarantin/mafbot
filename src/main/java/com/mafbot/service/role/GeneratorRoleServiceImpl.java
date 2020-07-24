package com.mafbot.service.role;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.role.Citizen;
import com.mafbot.role.Kattani;
import com.mafbot.role.Mafia;
import com.mafbot.role.Role;
import com.mafbot.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratorRoleServiceImpl implements GeneratorRoleService {
    private static final Logger log = LogManager.getLogger(GeneratorRoleServiceImpl.class);

    private OutgoingSender outgoingSender;

    public GeneratorRoleServiceImpl(OutgoingSender outgoingSender) {
        this.outgoingSender = outgoingSender;
    }

    @Override
    public void generateRoles(List<User> users) {
        int amountRoles = users.size();
        List<Role> roles = createRoleList(amountRoles);
        giveRolesUsers(users, roles);
    }

    private List<Role> createRoleList(int amount) {
        List<Role> result = new ArrayList<>();
        int gameNumber = BeanRepository.getInstance().getStatisticsService().getGameNumber();
        log.info("Начинаем генерацию ролей для игры №{} для количества игроков {}", gameNumber, amount);

        generateMafia(result, amount);
        generateKattani(result);
        generateCitizen(result, amount);

        Collections.shuffle(result);

        log.info("Роли для количества игроков {} в игре №{} успешно сгенерированы: {}", amount, gameNumber, result);
        return result;
    }

    private void giveRolesUsers(List<User> users, List<Role> roles) {
        int gameNumber = BeanRepository.getInstance().getStatisticsService().getGameNumber();
        log.info("Начинаем раздачу ролей {} игрокам в игре №{}", users.size(), gameNumber);
        //раздача ролей игрокам

        for (int i = 0; i < roles.size(); i++) {
            User user = users.get(i);
            Role role = roles.get(i);
            log.info("Даем роль {} игроку {}", role, user);
            user.setRole(role);

            //изменить на многопоточную доставку с подтверждением
            outgoingSender.sendDirectly(user.getChatIdPerson(), role.getGreetingText());
        }

        log.info("Роли для игры №{} успешно розданы!", gameNumber);
    }

    /**
     * Комиссар Каттани должен быть всегда 1.
     */
    private void generateKattani(List<Role> roles) {
        roles.add(new Kattani());
    }

    /**
     * В простейшей реализации количество мафиози равно половине игроков с округлением в меньшую сторону.
     */
    private void generateMafia(List<Role> roles, int amountPlayers) {
        int amountMafia = amountPlayers / 2;
        for (int i = 0; i < amountMafia; i++) {
            roles.add(new Mafia(i + 1));
        }
    }

    /**
     * Оставшиеся роли - мирные жители.
     */
    private void generateCitizen(List<Role> roles, int amountPlayers) {
        int amountCitizen = amountPlayers - roles.size();
        for (int i = 0; i < amountCitizen; i++) {
            roles.add(new Citizen());
        }
    }
}