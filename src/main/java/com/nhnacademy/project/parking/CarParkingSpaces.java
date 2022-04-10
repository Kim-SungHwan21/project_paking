package com.nhnacademy.project.parking;

public interface CarParkingSpaces {

    String scan(Car car);

    Car parking(Car car);

    Car exit(Car car);


}
