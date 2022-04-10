package com.nhnacademy.project.parking;

import static java.lang.Math.min;

import java.util.Objects;
import java.util.stream.Stream;

public enum Method {
    RATE() {
        @Override
        long getDiscountAmt(DiscountPolicy discountPolicy, long productAmt) {
            return (long) (productAmt * discountPolicy.getRate());
        }
    };

//    //        @JsonCreator
//    public static Method from(String value) {
//        return Stream.of(values())
//            .filter(e -> Objects.equals(e.value(), value))
//            .findAny()
//            .orElse(null);
//    }
//
//    //        @JsonValue
//    public String value() {
//        return name();
//    }

    abstract long getDiscountAmt(DiscountPolicy discountPolicy, long productAmt);

}
