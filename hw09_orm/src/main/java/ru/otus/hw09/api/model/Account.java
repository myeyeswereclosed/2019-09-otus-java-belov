package ru.otus.hw09.api.model;

public class Account {
    @Id
    private long no;
    private String type;
    private double rest;

    @Override
    public String toString() {
        return "Account{" + "id=" + no + ", type='" + type + "', rest = " + rest + '}';
    }

    public Account add(double sum) {
        rest += sum;

        return this;
    }

    public Account withdraw(double sum) {
        rest -= sum;

        return this;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRest(double rest) {
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public double getRest() {
        return rest;
    }
}
