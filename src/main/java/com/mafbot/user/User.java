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
    private Order order;

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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean hasRole() {
        return role != null;
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
        return "chatIdPerson=" + chatIdPerson +
                ", name='" + name;
    }
}
