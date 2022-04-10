package com.nhnacademy.project.parking;


import java.util.HashMap;
import java.util.Map;

public class HashMapCarParkingSpaces implements CarParkingSpaces{
    Map<String, Car> source = new HashMap<>();

    @Override
    public String scan(Car car){
        return car.getCarNumberPlate();
    }

    @Override
    public Car parking(Car car) {
        return source.put(car.getCarNumberPlate(), car);
    }

    @Override
    public Car exit(Car car) {
        return source.remove(car.getCarNumberPlate());
    }
}
