package ru.otus.hw10.api.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @OneToOne(targetEntity = AddressDataSet.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressDataSet address;

    @OneToMany(targetEntity = PhoneDataSet.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<PhoneDataSet> phones = new ArrayList<>();

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public User() {
    }

    public User(String name, AddressDataSet address) {
        this.name = name;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public User addPhone(PhoneDataSet phone) {
        phones.add(phone.setUser(this));

        return this;
    }

    public boolean livesAt(String street) {
        return Objects.nonNull(street) && street.equals(this.address.getStreet());
    }

    public boolean hasPhone(String number) {
        return
            Objects.nonNull(number)
                &&
            phones.stream().anyMatch(phone -> number.equals(phone.number()))
        ;
    }

    @Override
    public String toString() {
        return
            "User{" + "id=" + id + ", name='" + name + '\'' +
            ", address = " + address + ", phones:" + phones + '}'
        ;
    }
}
