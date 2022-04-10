package com.nhnacademy.project.parking;

public class NonePolicy implements Discountable {
    @Override
    public long getDiscountAmt(long originAmt) {
        return (long) (originAmt * 1);
    }
}
