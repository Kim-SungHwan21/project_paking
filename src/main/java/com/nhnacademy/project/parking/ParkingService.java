package com.nhnacademy.project.parking;

import java.util.Random;

public class PakingService {
    private final CarRepository repository;


    public PakingService(CarRepository repository) {
        this.repository = repository;
    }

    public void scan(Car car) { repository.insert(car); }

    public Car parking(String numberpate, CarType carType) {

    return null;
    }
}
