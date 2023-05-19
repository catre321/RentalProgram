package com.nguyenhoang.client;
import com.nguyenhoang.setvalue.Enum;
import com.nguyenhoang.stock.Item;
import java.util.ArrayList;

public class VIPAccount extends Customer{
    private int rewardPoint;

    public VIPAccount() {}

    public VIPAccount(String id, String name, String address, String phone, int totalSuccessfulRental, ArrayList<Item> items,
                      String username, String password, Enum.AccountType accountType, int rewardPoint) {
        super(id, name, address, phone, totalSuccessfulRental, items, username, password, accountType);
        this.rewardPoint = rewardPoint;
    }

    public int getRewardPoint() {
        return rewardPoint;
    }
    public void addRewardPoint() {
        this.rewardPoint += 10;
    }

    public boolean getFreeRental() {
        if (this.rewardPoint >= 100) {
            this.rewardPoint -= 100;
            return true;
        } else {
            return false;
        }
    }
}
