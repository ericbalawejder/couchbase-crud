package org.couchbase.quickstart.models;

public class Profile {

  private String pid;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private Integer balance;

  public Profile() {
  }

  public Profile(String pid, String firstName, String lastName, String email, String password, Integer balance) {
    this.pid = pid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.balance = balance;
  }

  public Profile(Profile profile) {
    this.pid = profile.getPid();
    this.firstName = profile.getFirstName();
    this.lastName = profile.getLastName();
    this.email = profile.getEmail();
    this.password = profile.getPassword();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Profile profile = (Profile) o;

    if (!pid.equals(profile.pid)) {
      return false;
    }
    if (!firstName.equals(profile.firstName)) {
      return false;
    }
    if (!lastName.equals(profile.lastName)) {
      return false;
    }
    if (!email.equals(profile.email)) {
      return false;
    }
    if (!password.equals(profile.password)) {
      return false;
    }
    return balance.equals(profile.balance);
  }

  @Override
  public int hashCode() {
    int result = pid.hashCode();
    result = 31 * result + firstName.hashCode();
    result = 31 * result + lastName.hashCode();
    result = 31 * result + email.hashCode();
    result = 31 * result + password.hashCode();
    result = 31 * result + balance.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Profile: { pid=" + this.pid + ",firstName=" + this.firstName + ",lastName=" + this.lastName + ",email="
        + this.email + ",password=" + this.password + ",balance=" + this.balance + " }";
  }

}
