package ru.otus.hw12.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private UserRole role;

    public User() {}

    public User(long id, String name, String login, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String name, String login, String password, UserRole role) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return role.equals(UserRole.ADMIN);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();

        result.put("id", String.valueOf(id));
        result.put("name", name);
        result.put("login", login);
        result.put("password", password);
        result.put("role", role.name());

        return result;
    }

    @Override
    public String toString() {
        return
            "User: {id = " + id + "; name = " + name +
            "; login = " + login + "; role = " + role +
            "; password = " + password + '}'
        ;
    }
}
