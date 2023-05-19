package com.nguyenhoang.stock;
import com.nguyenhoang.setvalue.Enum;
public class VideoGame extends Item {
    public VideoGame() {}

    public VideoGame(String id, String title, Enum.RentalType rentalType, Enum.LoanType loanType, int numCopies, double rentalFee, boolean rentalStatus) {
        super(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus);
    }

    @Override
    public String getId() {
        return super.getId();
    }
}
