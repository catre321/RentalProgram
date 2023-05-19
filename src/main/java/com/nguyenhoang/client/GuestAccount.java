package com.nguyenhoang.client;
import com.nguyenhoang.setvalue.Enum;
import java.util.ArrayList;
import com.nguyenhoang.stock.Item;

public class GuestAccount extends Customer{

    private int countInRental;

    public GuestAccount() {}

    public GuestAccount(String ID, String name, String address, String phone, int count, ArrayList<Item> items, String username, String password, Enum.AccountType accountType, int countInRental) {
        super(ID, name, address, phone, count, items, username, password, accountType);
        this.countInRental = countInRental;
    }

    public int getCountInRental() {
        return countInRental;
    }
    public void increaseCountInRental() {
        this.countInRental++;
    }

    public void reduceCountInRental() {
        this.countInRental--;
    }
}
