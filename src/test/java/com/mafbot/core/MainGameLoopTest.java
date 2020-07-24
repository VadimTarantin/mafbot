package com.mafbot.core;

import com.mafbot.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MainGameLoopTest {
    private MainGameLoop mainGameLoop;

    @Before
    public void setUp() {
        mainGameLoop = new MainGameLoop(null); //toDo: подключить PowerMockito
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
}