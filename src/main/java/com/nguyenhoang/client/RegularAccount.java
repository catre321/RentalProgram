package com.nguyenhoang.client;
import com.nguyenhoang.setvalue.Enum;
import java.util.ArrayList;
import com.nguyenhoang.stock.Item;

public class RegularAccount extends Customer{
    public RegularAccount() {}

    public RegularAccount(String id, String name, String address, String phone, int totalSuccessfulRental, ArrayList<Item> items, String username, String password, Enum.AccountType accountType) {
        super(id, name, address, phone, totalSuccessfulRental, items, username, password, accountType);
    }
}
