package co.edu.uptc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer implements Serializable {
    @Id
    @Column(name = "identification", nullable = false, unique = true)
    private String identification;
    private String name;
    private String lastName;
    private String address;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    private double balance;

    public Customer() {
    }

    public Customer(String identification, String name, String lastName, String address, String email,
            String phoneNumber, double balance) {
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Customer [identification=" + identification + ", name=" + name + ", lastName=" + lastName + ", address="
                + address + ", email=" + email + ", phoneNumber=" + phoneNumber + ", balance=" + balance + "]";
    }

}
