package com.mafbot.user;

import java.util.Objects;

public class User {
    private Long chatIdPerson;
    private String name;

    public User(Long chatIdPerson) {
        this.chatIdPerson = chatIdPerson;
    }

    public Long getChatIdPerson() {
        return chatIdPerson;
    }

    public void setChatIdPerson(Long chatIdPerson) {
        this.chatIdPerson = chatIdPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(chatIdPerson, user.chatIdPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatIdPerson);
    }

    @Override
    public String toString() {
        return "User{" +
                "chatIdPerson=" + chatIdPerson +
                ", name='" + name + '\'' +
                '}';
    }
}
