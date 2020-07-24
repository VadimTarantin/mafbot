package com.mafbot.core;

import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.role.Kattani;
import com.mafbot.role.Mafia;
import com.mafbot.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class MainGameLoopTest {
    private MainGameLoop mainGameLoop;

    @Before
    public void setUp() {
        OutgoingSender outgoingSender = PowerMockito.mock(OutgoingSender.class);
        mainGameLoop = new MainGameLoop(outgoingSender);
    }

    @Test
    public void testGetUsersListInGameWhenEmptyList() {
        String expected = mainGameLoop.getUsersListInGame();
        String actual = "В игре никого нет!";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetUsersListInGameWhenOnePlayer() {
        mainGameLoop.addUser(new User(42L, "firstName"));

        String actual = mainGameLoop.getUsersListInGame();
        String expected = "В игре: 1: firstName";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetUsersListInGameWhenTwoPlayers() {
        mainGameLoop.addUser(new User(112L, "firstName"));
        mainGameLoop.addUser(new User(298L, "secondName"));

        String actual = mainGameLoop.getUsersListInGame();
        String expected = "В игре: 1: firstName, 2: secondName";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetUsersListInStartGameWithRolesWhenTwoPlayers() {
        User firstUser = new User(112L, "firstName");
        firstUser.setRole(new Kattani());

        User secondUser = new User(298L, "secondName");
        secondUser.setRole(new Mafia(1));

        mainGameLoop.addUser(firstUser);
        mainGameLoop.addUser(secondUser);
        mainGameLoop.copyUsersInStartingListGame();

        String actual = mainGameLoop.getUsersListInStartGameWithRoles();
        String expected = "1: firstName - комиссар Каттани, 2: secondName - Мафиози 1";

        Assert.assertEquals(expected, actual);
    }
}