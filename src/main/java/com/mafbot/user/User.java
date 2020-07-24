package com.mafbot.user;

import com.mafbot.role.Role;

import java.util.Objects;

/**
 * Представление пользователя в боте.
 */
public class User {
    private Long chatIdPerson;
    private String name;
    private Role role;

    public User(Long chatIdPerson) {
        this.chatIdPerson = chatIdPerson;
    }

    public User(Long chatIdPerson, String name) {
        this.chatIdPerson = chatIdPerson;
        this.name = name;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
