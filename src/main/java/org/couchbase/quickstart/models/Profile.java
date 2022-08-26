package org.couchbase.quickstart.models;

import java.util.Objects;
import java.util.StringJoiner;

public class Profile {

  private String pid;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String salt;
  private Integer balance;

  public Profile() {
  }

  public Profile(
      String pid, String firstName, String lastName, String email, String password, String salt, Integer balance) {
    this.pid = pid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.salt = salt;
    this.balance = balance;
  }

  public Profile(Profile profile) {
    this.pid = profile.getPid();
    this.firstName = profile.getFirstName();
    this.lastName = profile.getLastName();
    this.email = profile.getEmail();
    this.password = profile.getPassword();
    this.salt = profile.getSalt();
    this.balance = profile.getBalance();
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profile profile = (Profile) o;
    return Objects.equals(pid, profile.pid) && Objects.equals(firstName, profile.firstName)
        && Objects.equals(lastName, profile.lastName) && Objects.equals(email, profile.email)
        && Objects.equals(password, profile.password) && Objects.equals(salt, profile.salt)
        && Objects.equals(balance, profile.balance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pid, firstName, lastName, email, password, salt, balance);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Profile.class.getSimpleName() + "[", "]")
        .add("pid='" + pid + "'")
        .add("firstName='" + firstName + "'")
        .add("lastName='" + lastName + "'")
        .add("email='" + email + "'")
        .add("password='" + password + "'")
        .add("salt='" + salt + "'")
        .add("balance=" + balance)
        .toString();
  }

}
