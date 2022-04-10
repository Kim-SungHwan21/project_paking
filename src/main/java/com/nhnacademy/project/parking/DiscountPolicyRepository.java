package com.nhnacademy.project.parking;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface DiscountPolicyRepository {

    void insert(DiscountPolicy source);

    DiscountPolicy findByCode(String code);

    default List<Discountable> findByCodes(List<String> codes) {
        return codes.stream()
            .map(this::findByCode)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }


    void deleteByCode(String code);
}


