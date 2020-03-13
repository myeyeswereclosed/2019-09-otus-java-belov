package ru.otus.hw16.lib.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    @Column(name = "name")
    @NotNull
    @NotBlank
    private String name;

    @Column(name = "login", unique = true)
    @NotNull
    @NotBlank
    private String login;

    @Column(name = "password")
    @NotNull
    @NotBlank
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    public User() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.getId();
    }

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

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isClient() {
        return role.equals(UserRole.CLIENT);
    }

    public boolean isAdmin() {
        return role.equals(UserRole.ADMIN);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name ='" + name + "', login='" + login + "', role = '" + role +  "\'}";
    }
}
