package com.nhnacademy.project.parking;

public class PaycoDiscountPolicy implements Discountable {
    @Override
    public long getDiscountAmt(long originAmt) {
        return (long) (originAmt * 0.1);
    }
}
