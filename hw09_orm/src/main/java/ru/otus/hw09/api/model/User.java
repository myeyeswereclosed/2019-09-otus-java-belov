package ru.otus.hw09.api.model;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class User {
  @Id
  private long id = 0;
  private String name = "";
  private int age;

  public User(long id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public User(){}

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public User becomeOlder(int years) {
      this.age += years;

      return this;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + "', age = " + age + '}';
  }
}
