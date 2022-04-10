package com.nhnacademy.project.parking;

import static com.nhnacademy.project.parking.CarType.MEDIUM;
import static com.nhnacademy.project.parking.CarType.SMALL;
import static java.lang.Thread.sleep;

import java.time.LocalDateTime;
import java.util.Objects;

class Main {
    public static void main(String[] args) throws InterruptedException {

        Thread entrance = new Thread(new Entrance());
        Thread entrance2 = new Thread(new Entrance2());
        Thread exit = new Thread(new Exit());
        Thread exit2 = new Thread(new Exit2());

        entrance.start();
        entrance.join();

        exit.start();
        exit.join();

        entrance2.start();
        entrance2.join();

        exit2.start();
        exit2.join();

    }

    static LocalDateTime enterTime;
    static CarParkingSpaces carParkingSpaces;
    static ParkingService parkingService = new ParkingService(carParkingSpaces);
    static LocalDateTime exitTime;
    static Car car1;
    static Car car2;
    static long money;
    static long money2;
    static long beforeMoney;
    static long afterMoney;
    static long charge;
    static long beforeMoney2;
    static long afterMoney2;
    static long charge2;
    static DiscountPolicy payco = DiscountPolicy.rate("PAYCO", 0.1f);
    static DiscountPolicy none = DiscountPolicy.rate("NONE", 0f);
    static MapDiscountPolicyRepository repository = new MapDiscountPolicyRepository();
    static PaymentService paymentService = new PaymentService(repository);
    static Discount discount;
    static Discount discount2;

    static class Entrance implements Runnable {

        @Override
        public void run() {
            repository.insert(payco);
            enterTime = LocalDateTime.now();

            try {
                sleep(2000);
                exitTime = LocalDateTime.now();
                System.out.println(exitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            money = 50_000L;
            car1 = parkingService.scan(new Car("12가1234", MEDIUM, money, payco, enterTime, exitTime));
            System.out.println(car1.getCarNumberPlate() + " CAR1: " + car1.enterTime + " 1번 출구로 입차되었습니다");
            car1.parkingService.parking(car1);
            System.out.println("CAR1가 A-1구역에 주차했습니다.");
        }
    }

    static class Entrance2 implements Runnable {
        @Override
        public void run() {
            repository.insert(none);
            enterTime = LocalDateTime.now();
            try {
                sleep(5000);
                exitTime = LocalDateTime.now();
                System.out.println(exitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            money2 = 100_000L;
            car2 = parkingService.scan(new Car("98가9876", SMALL, money2, none, enterTime, exitTime));
            System.out.println(car2.getCarNumberPlate() + " CAR2: " + car2.enterTime + " 2번 출구로 입차되었습니다");
            car2.parkingService.parking(car2);
            System.out.println("CAR2가 A-2구역에 주차했습니다.");
        }
    }

    static class Exit implements Runnable {
        @Override
        public void run() {
            try {
                beforeMoney = car1.getMoney();
                sleep(1000);
                parkingService.exit(car1);
                System.out.println(car1.getCarNumberPlate() + " CAR1: " + car1.exitTime + " 1번 출구로 출차되었습니다.");
                afterMoney = car1.getMoney();
                charge = beforeMoney - afterMoney;
            } catch (Exception e) {
                e.printStackTrace();
            }
            discount = paymentService.getDiscount(charge, "PAYCO");
            System.out.println(car1.getCarNumberPlate() + "페이코 회원 10%할인 적용 금액: "
                + discount + ", CAR1잔액: " + afterMoney + "원");
        }
    }
    static class Exit2 implements Runnable {
        @Override
        public void run() {
            try {
                beforeMoney2 = car2.getMoney();
                sleep(5000);
                parkingService.exit(car2);
                System.out.println(car2.getCarNumberPlate() + " CAR2: " + car2.exitTime + " 2번 출구로 출차되었습니다.");
                afterMoney2 = car2.getMoney();
                charge2 = beforeMoney2 - afterMoney2;
            } catch (Exception e) {
                e.printStackTrace();
            }
            discount2 = paymentService.getDiscount(charge2, "None");
            System.out.println(car2.getCarNumberPlate() + "비회원은 할인 적용이 안됩니다. 적용 금액: "
                + discount + ", CAR2잔액: " + afterMoney + "원");
        }
    }
}
public class Car {
    private DiscountPolicy code;
    private String numberPlate;
    private CarType carType;
    private static long money;
    private CarParkingSpaces carParkingSpaces;
    LocalDateTime exitTime;
    LocalDateTime enterTime;
    ParkingService parkingService = new ParkingService(carParkingSpaces);

    public Car(String numberPlate, CarType carType, long money,
               DiscountPolicy code, LocalDateTime enterTime,
               LocalDateTime exitTime) {
        this.numberPlate = numberPlate;
        this.carType = carType;
        this.code = code;
        this.money = money;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return Objects.equals(numberPlate, car.numberPlate) && carType == car.carType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberPlate, carType);
    }

    public String getCarNumberPlate() {
        return numberPlate;
    }

    public static long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public CarType getCarType() {
        return this.carType = carType;
    }
}