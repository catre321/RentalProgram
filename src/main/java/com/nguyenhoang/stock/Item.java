package com.nguyenhoang.stock;
import com.nguyenhoang.setvalue.Enum;

import java.util.ArrayList;

public class Item {
    protected String id;
    protected String title;
    protected Enum.RentalType rentalType;
    protected Enum.LoanType loanType;
    protected int numCopies;
    protected double rentalFee;
    protected boolean rentalStatus;

    public Item() {}

    public Item(String id, String title, Enum.RentalType rentalType, Enum.LoanType loanType, int numCopies, double rentalFee, boolean rentalStatus) {
        this.id = id;
        this.title = title;
        this.rentalType = rentalType;
        this.loanType = loanType;
        this.numCopies = numCopies;
        this.rentalFee = rentalFee;
        this.rentalStatus = rentalStatus;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Enum.RentalType getRentalType() {
        return rentalType;
    }

    public Enum.LoanType getLoanType() {
        return loanType;
    }

    public int getNumCopies() {
        return numCopies;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public String getYearPublished() {
        return id.substring(5);
    }

    public int getIdNumber() {
        return Integer.parseInt(id.substring(1, 4));
    }

    @Override
    public String toString() {
        if(rentalType == Enum.RentalType.RECORD || rentalType == Enum.RentalType.DVD){
            return "Item{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", rentalType=" + rentalType +
                    ", loanType=" + loanType +
                    ", numCopies=" + numCopies +
                    ", rentalFee=" + rentalFee +
                    ", rentalStatus=" + rentalStatus +
                    '}';
        }
        else {
            return "Item{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", rentalType=" + rentalType +
                    ", loanType=" + loanType +
                    ", numCopies=" + numCopies +
                    ", rentalFee=" + rentalFee +
                    ", rentalStatus=" + rentalStatus +
                    '}';
        }
    }

    public void reduceNumCopies() {
        numCopies--;
    }

    public void increaseNumCopies() {
        numCopies++;
    }
}

