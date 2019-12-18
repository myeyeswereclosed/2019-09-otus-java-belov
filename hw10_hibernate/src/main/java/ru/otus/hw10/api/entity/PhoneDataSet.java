package ru.otus.hw10.api.entity;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class PhoneDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PhoneDataSet(){}

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public PhoneDataSet setUser(User user) {
        this.user = user;

        return this;
    }

    public String number() {
        return number;
    }

    @Override
    public String toString() {
        return "Phone:{" + number +  '}';
    }
}
