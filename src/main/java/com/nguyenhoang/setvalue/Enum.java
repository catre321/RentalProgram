package com.nguyenhoang.setvalue;
public record Enum () {
    public enum AccountType {
        GUEST(1), REGULAR(2), VIP(3);

        private final int level;
        AccountType(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }
    public enum RentalType {
        RECORD, DVD, GAME
    }
    public enum LoanType {
        TWODAYS, ONEWEEK
    }
    public enum Genre {
        ACTION, HORROR, DRAMA, COMEDY
    }
}
