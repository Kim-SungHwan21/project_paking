package com.nhnacademy.project.parking;

import static com.nhnacademy.project.parking.CarType.LIGHT;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ParkingService {
    private final CarParkingSpaces carParkingSpaces;
    private final HashMapCarParkingSpaces hashMapCarParkingSpaces = new HashMapCarParkingSpaces();
    LocalDateTime exitTime;
    LocalDateTime enterTime;

    public ParkingService(CarParkingSpaces carParkingSpaces) {
        this.carParkingSpaces = carParkingSpaces;
    }

    public Car scan(Car car) {
        hashMapCarParkingSpaces.scan(car);
        return car;
    }

    public Car parking(Car car) {
        hashMapCarParkingSpaces.parking(car);
        return car;
    }

    public Car exit(Car car) throws Exception {
        long parkingDuration = ChronoUnit.MILLIS.between(car.enterTime, car.exitTime);
        if (parkingDuration < 1800000) {
            car.setMoney(car.getMoney());
        } else if (parkingDuration > 1800000) {
            long count = 1;
            if (car.getCarType() == LIGHT) {
                car.setMoney(car.getMoney() - (1000L + (count * 500))/2);
            } else {
                car.setMoney(car.getMoney() - 1000L + (count * 500));
            }
            if (car.getMoney() < 0) {
                throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
            }
            if ((parkingDuration - 3600000) / 600000 > 0) {
                count = (parkingDuration - 3600000) / 600000;
                if (car.getCarType() == LIGHT) {
                    car.setMoney(car.getMoney() - (1000L + (count * 500))/2);
                } else {
                    car.setMoney(car.getMoney() - 1000L + (count * 500));
                }
                if (car.getMoney() < 0) {
                    throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
                }
            }
//            if ((parkingDuration - 1800000) / 600000 > 3) {
//                car.setMoney(car.getMoney() - 3000L);
//                if (car.getMoney() < 0) {
//                    throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
//                }
//            }
            if ((parkingDuration - 20400000) >= 0) {
                if (car.getCarType() == LIGHT) {
                    car.setMoney(car.getMoney() - 15000L / 2L);
                } else {
                    car.setMoney(car.getMoney() - 15000L);
                }
                if (car.getMoney() < 0) {
                    throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
                }
            }
            if ((parkingDuration - 86400000) > 0) {
                if (car.getCarType() == LIGHT) {
                    car.setMoney(car.getMoney() - 30000L / 2L);
                } else {
                    car.setMoney(car.getMoney() - 30000L);
                }
                if (car.getMoney() < 0) {
                    throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
                }
            }
        }
        return hashMapCarParkingSpaces.exit(car);
    }
}

