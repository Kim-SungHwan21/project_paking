package com.nhnacademy.project.paking;

import static com.nhnacademy.project.paking.CarType.LARGE;
import static com.nhnacademy.project.paking.CarType.LIGHT;
import static com.nhnacademy.project.paking.CarType.MEDIUM;
import static com.nhnacademy.project.paking.CarType.SMALL;

public class Car {

    private String numberplate;
    private CarType carType;

    public Car(String numberplate, CarType carType) {
        this.numberplate = numberplate;
        this.carType = carType;
    }

    public static Car large(String numberplate) {
        return new Car(numberplate, LARGE); }

    public static Car medium(String numberplate) {
        return new Car(numberplate, MEDIUM); }

    public static Car small(String numberplate) {
        return new Car(numberplate, SMALL); }

    public static Car light(String numberplate) {
        return new Car(numberplate, LIGHT); }

    public void CarComesIn() {

    }


}
