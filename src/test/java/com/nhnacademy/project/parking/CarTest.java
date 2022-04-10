package com.nhnacademy.project.parking;

import static com.nhnacademy.project.parking.CarType.LIGHT;
import static com.nhnacademy.project.parking.Main.payco;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CarTest {
    //private ParkingService service;
    private CarParkingSpaces carParkingSpaces;
    private Thread thread;
    private Main.Entrance entrance;
    private Main.Entrance2 entrance2;
    private Main.Exit exit;
    private Main.Exit2 exit2;

    @BeforeEach
    void setUp() {
        carParkingSpaces = mock(CarParkingSpaces.class);
        thread = mock(Thread.class);
        entrance = mock(Main.Entrance.class);
        entrance2 = mock(Main.Entrance2.class);
        exit = mock(Main.Exit.class);
        exit2 = mock(Main.Exit2.class);
    }

    @DisplayName("주차장에 차가 들어오면 번호판을 Scan하는 테스트")
    @Test
    void scan() {
        String numberPlate = "김해11가1234";
        long money = 50_000L;
        String[] spaces = new String[10];
        spaces[0] = numberPlate;
        LocalDateTime enterTime;
        LocalDateTime exitTime = null;

        Car car = new Car(numberPlate, CarType.MEDIUM, money, payco, enterTime = LocalDateTime.now(), exitTime);

        assertThat(spaces[0]).isEqualTo(car.getCarNumberPlate());
    }

    @DisplayName("차가 들어오면 주차장에 차가 주차되는 테스트")
    @Test
    void parking() {
        String numberPlate = "김해11가1234";
        long money = 50_000L;
        LocalDateTime enterTime;
        LocalDateTime exitTime = null;

        Car car = new Car(numberPlate, CarType.MEDIUM, money, payco, enterTime = LocalDateTime.now(), exitTime);

        when(carParkingSpaces.parking(car)).thenReturn(car);

        Car newCar = carParkingSpaces.parking(new Car(numberPlate, CarType.MEDIUM, money, payco,
            enterTime = LocalDateTime.now(), exitTime));

        assertThat(newCar).isEqualTo(car);
    }

    @DisplayName("출차 시 요금을 계산하는 테스트, money가 부족할 경우 출차를 할 수 없음.")
    @Test
    void exit() throws Exception {
        String numberPlate = "김해11가1234";
        long money1 = 50000L;
        long money2 = 50000L;
        long money3 = 50000L;
        long money4 = 50000L;
        long money5 = 50000L;
        long money6 = 50000L;

        LocalDateTime enterTime = LocalDateTime.of(2022,4,8,13,0,0,0);
        LocalDateTime exitTime = LocalDateTime.of(2022,4,8,13,29,0,0);
        LocalDateTime exitTime2 = LocalDateTime.of(2022,4,8,13,30,1,0);
        LocalDateTime exitTime3 = LocalDateTime.of(2022,4,8,13,50,0,0);
        LocalDateTime exitTime4 = LocalDateTime.of(2022,4,8,14,1,0,0);
        LocalDateTime exitTime5 = LocalDateTime.of(2022,4,8,19,0,0,0);
        LocalDateTime exitTime6 = LocalDateTime.of(2022,4,9,13,0,1,0);

        Car car = new Car(numberPlate, CarType.MEDIUM, money1, payco, enterTime, exitTime);
        Car car2 = new Car(numberPlate, CarType.MEDIUM, money2, payco, enterTime, exitTime2);
        Car car3 = new Car(numberPlate, CarType.MEDIUM, money3, payco, enterTime, exitTime3);
        Car car4 = new Car(numberPlate, CarType.MEDIUM, money4, payco, enterTime, exitTime4);
        Car car5 = new Car(numberPlate, CarType.MEDIUM, money5, payco, enterTime, exitTime5);
        Car car6 = new Car(numberPlate, CarType.MEDIUM, money6, payco, enterTime, exitTime6);

        carParkingSpaces.scan(car);
        carParkingSpaces.scan(car2);
        carParkingSpaces.scan(car3);
        carParkingSpaces.scan(car4);
        carParkingSpaces.scan(car5);
        carParkingSpaces.scan(car6);

        long parkingDuration1;
        long parkingDuration2;
        long parkingDuration3;
        long parkingDuration4;
        long parkingDuration5;
        long parkingDuration6;

        parkingDuration1 = ChronoUnit.MILLIS.between(enterTime, exitTime);        //30분 미만, 1,800,000 millis
        parkingDuration2 = ChronoUnit.MILLIS.between(enterTime, exitTime2);       //30분 1초, 1,801,000 millis
        parkingDuration3 = ChronoUnit.MILLIS.between(enterTime, exitTime3);       //50분, 3,000,000 millis
        parkingDuration4 = ChronoUnit.MILLIS.between(enterTime, exitTime4);       //61분, 3,660,000 millis
        parkingDuration5 = ChronoUnit.MILLIS.between(enterTime, exitTime5);       //6시간, 21,600,000 millis
        parkingDuration6 = ChronoUnit.MILLIS.between(enterTime, exitTime6);       //24시간 1초, 86,401,000 millis

        if (parkingDuration1 < 1800000) {
            car.setMoney(car.getMoney() - 1000L);
            if (car.getMoney() < 0) {
                throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
            }
            carParkingSpaces.exit(car);
        }
        if ((parkingDuration2 - 1800000) > 999) {
            //long result2 = (case2 - 1800000) / 1000;
            car2.setMoney(car2.getMoney() - 1500L);
            if (car2.getMoney() < 0) {
                throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
            }
            carParkingSpaces.exit(car2);
        }
        if ((parkingDuration3 - 1800000) / 600000 > 0) {
            long result3 = (parkingDuration3 - 1800000) / 600000;
            car3.setMoney(car3.getMoney() - (result3 * 500L + 1000L));
            if (car3.getMoney() < 0) {
                throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
            }
            carParkingSpaces.exit(car3);
        }
        if ((parkingDuration4 - 1800000) / 600000 >= 3) {
            car4.setMoney(car4.getMoney() - 3000L);
            if (car4.getMoney() < 0) {
                throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
            }
            carParkingSpaces.exit(car4);
        }
        if ((parkingDuration5 - 21600000) >= 0) {
            car5.setMoney(car5.getMoney() - 10000L);
            if (car5.getMoney() < 0) {
                throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
            }
            carParkingSpaces.exit(car5);
        }
        if ((parkingDuration6 - 86400000) > 0) {
            car6.setMoney(car6.getMoney() - 20000L);
            if (car6.getMoney() < 0) {
                throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
            }
            carParkingSpaces.exit(car6);
        }
        assertThat(car.getMoney()).isEqualTo(49000L);
        assertThat(car2.getMoney()).isEqualTo(48500L);
        assertThat(car3.getMoney()).isEqualTo(48000L);
        assertThat(car4.getMoney()).isEqualTo(47000L);
        assertThat(car5.getMoney()).isEqualTo(40000L);
        assertThat(car6.getMoney()).isEqualTo(30000L);
    }

//    @DisplayName("n개의 입구와 n개의 출구 테스트")
//    @Test
//    void multipleEnterAndExit() throws InterruptedException {
//
//        LocalDateTime enterTime;
//        ParkingService parkingService = new ParkingService(carParkingSpaces);
//        LocalDateTime exitTime;
//        Car car1;
//        Car car2;
//        Car newCar1;
//        Car newCar2;
//
//        Thread entrance = new Thread(new Main.Entrance());
//        Thread entrance2 = new Thread(new Main.Entrance2());
//        Thread exit = new Thread(new Main.Exit());
//        Thread exit2 = new Thread(new Main.Exit2());
//
//        enterTime = LocalDateTime.of(2022, 4, 8, 13, 0, 0, 0);
//        exitTime = LocalDateTime.of(2022, 4, 8, 13, 29, 0, 0);
//
//        car1 = parkingService.scan(new Car("12가1234", MEDIUM, 50000L, enterTime, exitTime));
//
//        LocalDateTime enterTime2 = LocalDateTime.of(2022,4,8,13,0,0,0);
//        LocalDateTime exitTime2 = LocalDateTime.of(2022,4,8,19,0,0,0);
//
//        car2 = parkingService.scan(new Car("98나9876", MEDIUM, 50000L, enterTime2, exitTime2));
//
    //        when(parkingService.scan(new Car("12가1234", MEDIUM, 50000L, enterTime, exitTime))).thenReturn(car1);
//        when(parkingService.scan(new Car("98나9876", MEDIUM, 50000L, enterTime2, exitTime2))).thenReturn(car2);
//
//        newCar1 = parkingService.scan(new Car("12가1234", MEDIUM, 50000L, enterTime, exitTime));
//        newCar2 = parkingService.scan(new Car("98나9876", MEDIUM, 50000L, enterTime, exitTime));
//        entrance.start();
//        entrance.join();
//
//        entrance2.start();
//        entrance2.join();
//
//        exit.start();
//        exit.join();
//
//        exit2.start();
//        exit2.join();
//
//        assertThat(newCar1).isEqualTo(car1);
//        assertThat(newCar2).isEqualTo(car2);
//
//        class Main {
////            LocalDateTime enterTime;
////            CarParkingSpaces carParkingSpaces;
////            ParkingService parkingService; // = new ParkingService(carParkingSpaces);
////            LocalDateTime exitTime;
////            Car car1;
////            Car car2;
////            Car newCar1;
////            Car newCar2;
//
//            class Entrance3 implements Runnable {
//
//                @Override
//                public void run() {
//                    LocalDateTime enterTime = LocalDateTime.of(2022,4,8,13,0,0,0);
//                    LocalDateTime exitTime = LocalDateTime.of(2022,4,8,13,29,0,0);
//
////                    car1 = parkingService.scan(new Car("12가1234", MEDIUM, 50000L, enterTime, exitTime));
//
////                    when(parkingService.scan(new Car("12가1234", MEDIUM, 50000L, enterTime, exitTime))).thenReturn(car1);
//
////                    Car newCar1 = parkingService.scan(new Car("12가1234", MEDIUM, 50000L, enterTime, exitTime));
//                    System.out.println(car1.getCarNumberPlate() + " CAR1: " + car1.enterTime + " 1번 출구로 입차되었습니다");
//                    car1.parkingService.parkng(car1);
//                    }
//                }
//
//
//            class Entrance4 implements Runnable {
//
//                @Override
//                public void run() {
//                    LocalDateTime enterTime = LocalDateTime.of(2022,4,8,13,0,0,0);
//                    LocalDateTime exitTime = LocalDateTime.of(2022,4,8,19,0,0,0);
//
////                    car2 = parkingService.scan(new Car("98나9876", MEDIUM, 50000L, enterTime, exitTime));
//
////                    when(parkingService.scan(new Car("98나9876", MEDIUM, 50000L, enterTime, exitTime))).thenReturn(car2);
//
////                    Car newCar2 = parkingService.scan(new Car("98나9876", MEDIUM, 50000L, enterTime, exitTime));
//                    System.out.println(car2.getCarNumberPlate() + " CAR1: " + car2.enterTime + " 1번 출구로 입차되었습니다");
//                    car2.parkingService.parkng(car2);
//                }
//            }
//
//            class Exit3 implements Runnable {
//
//                @Override
//                public void run() {
//                    try {
//                        parkingService.exit(car1);
//                        System.out.println(car1.getCarNumberPlate() + " CAR1: " + car1.exitTime + " 1번 출구로 출차되었습니다.");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(car1.getCarNumberPlate() + " CAR1잔액: " + car1.getMoney());
//
//                }
//            }
//
//            class Exit4 implements Runnable {
//
//                @Override
//                public void run() {
//                    try {
//                        parkingService.exit(car2);
//                        System.out.println(car2.getCarNumberPlate() + " CAR1: " + car2.exitTime + " 1번 출구로 출차되었습니다.");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
////                    assertThat(newCar2).isEqualTo(car2);
//                    System.out.println(car2.getCarNumberPlate() + " CAR1잔액: " + car2.getMoney());
//                }
//            }
//        }
//
//    }

    @DisplayName("출차 시 변경된 요금표 테스트")
    @Test
    void changeExit() throws Exception {
        String numberPlate = "김해11가1234";
        long money1 = 50000L;
        long money2 = 50000L;
        long money3 = 50000L;
        long money4 = 50000L;
        long money5 = 50000L;
        long money6 = 50000L;

        LocalDateTime enterTime = LocalDateTime.of(2022, 4, 8, 13, 0, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 8, 13, 29, 0, 0);
        LocalDateTime exitTime2 = LocalDateTime.of(2022, 4, 8, 13, 30, 1, 0);
        LocalDateTime exitTime3 = LocalDateTime.of(2022, 4, 8, 14, 1, 0, 0);
        LocalDateTime exitTime4 = LocalDateTime.of(2022, 4, 8, 18, 40, 0, 0);
        LocalDateTime exitTime5 = LocalDateTime.of(2022, 4, 9, 13, 0, 1, 0);
        LocalDateTime exitTime6 = LocalDateTime.of(2022, 4, 9, 13, 0, 1, 0);

        Car car = new Car(numberPlate, CarType.MEDIUM, money1,  payco, enterTime, exitTime);
        Car car2 = new Car(numberPlate, CarType.MEDIUM, money2, payco, enterTime, exitTime2);
        Car car3 = new Car(numberPlate, CarType.MEDIUM, money3, payco, enterTime, exitTime3);
        Car car4 = new Car(numberPlate, CarType.MEDIUM, money4, payco, enterTime, exitTime4);
        Car car5 = new Car(numberPlate, CarType.MEDIUM, money5, payco, enterTime, exitTime5);
        Car car6 = new Car(numberPlate, CarType.MEDIUM, money6, payco, enterTime, exitTime6);

        carParkingSpaces.scan(car);
        carParkingSpaces.scan(car2);
        carParkingSpaces.scan(car3);
        carParkingSpaces.scan(car4);
        carParkingSpaces.scan(car5);
        carParkingSpaces.scan(car6);

        long parkingDuration1;
        long parkingDuration2;
        long parkingDuration3;
        long parkingDuration4;
        long parkingDuration5;
        long parkingDuration6;

        parkingDuration1 =
            ChronoUnit.MILLIS.between(enterTime, exitTime);        //30분 미만, 1,800,000 millis
        parkingDuration2 =
            ChronoUnit.MILLIS.between(enterTime, exitTime2);       //30분 1초, 1,801,000 millis
        parkingDuration3 =
            ChronoUnit.MILLIS.between(enterTime, exitTime3);       //61분, 3,660,000 millis
        parkingDuration4 =
            ChronoUnit.MILLIS.between(enterTime, exitTime4);       //5시간40분, 20,400,000 millis
        parkingDuration5 =
            ChronoUnit.MILLIS.between(enterTime, exitTime5);       //24시간 1초, 86,401,000 millis
        parkingDuration6 =
            ChronoUnit.MILLIS.between(enterTime, exitTime6);       //경차, 24시간 1초, 86,401,000 millis

        if (parkingDuration1 < 1800000) {
            car.setMoney(car.getMoney());
            carParkingSpaces.exit(car);
        }
        if ((parkingDuration2 - 1800000) > 999) {
            if (car2.getCarType() == LIGHT) {
                car2.setMoney(car2.getMoney() - 1000L / 2L);
            } else {
                car2.setMoney(car2.getMoney() - 1000L);
            }
            if (car2.getMoney() < 0) {
                throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
            }
            carParkingSpaces.exit(car2);
        }
        if ((parkingDuration3 - 3600000) / 600000 > 0) {
            long result3 = (parkingDuration3 - 3600000) / 600000;
            if (car3.getCarType() == LIGHT) {
                car3.setMoney(car3.getMoney() - (result3 * 500L + 1000L) / 2);
            } else {
                car3.setMoney(car3.getMoney() - (result3 * 500L + 1000L));
                //}
                if (car3.getMoney() < 0) {
                    throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
                }
                carParkingSpaces.exit(car3);
            }
            if ((parkingDuration4 - 20400000) >= 0) {
                if (car4.getCarType() == LIGHT) {
                    car4.setMoney(car4.getMoney() - 15000L / 2L);
                } else {
                    car4.setMoney(car4.getMoney() - 15000L);
                }
                if (car4.getMoney() < 0) {
                    throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
                }
                carParkingSpaces.exit(car4);
            }
            if ((parkingDuration5 - 86400000) > 0) {
                if (car5.getCarType() == LIGHT) {
                    car5.setMoney(car5.getMoney() - 30000L / 2L);
                } else {
                    car5.setMoney(car5.getMoney() - 30000L);
                }
                if (car5.getMoney() < 0) {
                    throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
                }
                carParkingSpaces.exit(car5);
            }
            if ((parkingDuration6 - 86400000) > 0) {
                if (car6.getCarType() == LIGHT) {
                    car6.setMoney(car6.getMoney() - 30000L / 2L);
                } else {
                    car6.setMoney(car6.getMoney() - 30000L);
                }
                if (car6.getMoney() < 0) {
                    throw new Exception("결제가 되지 않아 주차장을 나갈 수 없습니다.");
                }
                carParkingSpaces.exit(car6);
            }

            assertThat(car.getMoney()).isEqualTo(50000L);
            assertThat(car2.getMoney()).isEqualTo(49000L);
            assertThat(car3.getMoney()).isEqualTo(48500L);
            assertThat(car4.getMoney()).isEqualTo(35000L);
            assertThat(car5.getMoney()).isEqualTo(20000L);
            assertThat(car6.getMoney()).isEqualTo(35000L);


        }


    }


}