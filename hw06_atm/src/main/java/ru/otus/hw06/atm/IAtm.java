package ru.otus.hw06.atm;

public interface IAtm {
    String withdraw(int sum);

    String insert(Banknote banknote);

    String remains();
}
