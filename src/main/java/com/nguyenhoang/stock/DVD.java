package com.nguyenhoang.stock;
import com.nguyenhoang.setvalue.Enum;
public class DVD extends Item {
    private Enum.Genre genre;

    public DVD() {}

    public DVD(String id, String title, Enum.RentalType rentalType, Enum.LoanType loanType, int numCopies, double rentalFee, boolean rentalStatus, Enum.Genre genre) {
        super(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus);
        this.genre = genre;
    }

    public Enum.Genre getGenre() {
        return genre;
    }

    @Override
    public String getId() {
        return super.getId();
    }
}
