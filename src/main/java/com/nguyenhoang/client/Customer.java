package com.nguyenhoang.client;
import com.nguyenhoang.setvalue.Enum;
import java.util.ArrayList;

import com.nguyenhoang.stock.Item;

public class Customer {
    protected String id;
    protected String name;
    protected String address;
    protected String phone;
    protected int totalSuccessfulRental;
    protected ArrayList<Item> items = new ArrayList<>();
    protected String username;
    protected String password;
    protected Enum.AccountType accountType;

    public Customer() {}

    public Customer(String id, String name, String address, String phone, int totalSuccessfulRental, ArrayList<Item> items, String username, String password, Enum.AccountType accountType) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.totalSuccessfulRental = totalSuccessfulRental;
        this.items = items;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", totalSuccessfulRental=" + totalSuccessfulRental +
                ", items=" + items +
                ", usernameLogIn='" + username + '\'' +
                ", passwordLogIn='" + password + '\'' +
                ", accountType=" + accountType +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public int getTotalSuccessfulRental() {
        return totalSuccessfulRental;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Enum.AccountType getAccountType() {
        return accountType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountType(Enum.AccountType accountType) {
        this.accountType = accountType;
    }

    public void rentAnItem(Item item) {
        this.items.add(item);
    }

    public void returnAnItem(Item item) {
        this.items.remove(item);
    }

    public void increaseTotalSuccessfulRental() {
        this.totalSuccessfulRental++;
    }

}
