package com.nhnacademy.project.parking;

import java.util.HashMap;
import java.util.Map;

public class MapDiscountPolicyRepository implements DiscountPolicyRepository {
    Map<String, DiscountPolicy> source = new HashMap<>();

    @Override
    public void insert(DiscountPolicy policy) {
        source.put(policy.getCode(), policy);
    }

    @Override
    public DiscountPolicy findByCode(String code) {
        return source.get(code);
    }

    @Override
    public void deleteByCode(String code) {
        source.remove(code);
    }

}
