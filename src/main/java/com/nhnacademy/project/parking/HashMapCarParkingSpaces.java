package com.nhnacademy.project.parking;


import java.util.HashMap;
import java.util.Map;

public class HashMapCarParkingSpaces implements CarParkingSpaces{
    Map<String, Car> source = new HashMap<>();
    StringBuilder repository;

    @Override
    public Car scan(Car car){
        return source.put(car.getCarNumberPlate(), car);
    }


}
