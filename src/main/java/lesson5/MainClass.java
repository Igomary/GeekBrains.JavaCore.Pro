package lesson5;



import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.CyclicBarrier;


public class MainClass {
    public static final int CARS_COUNT = 4;
    private static final CountDownLatch START = new CountDownLatch(CARS_COUNT);
    private static final CountDownLatch STOP = new CountDownLatch(CARS_COUNT);
  //  private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);


    public static void main(String[] args) {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10),START, STOP);
        }

        try{
            for (Car car : cars) {
                new Thread(car).start();
            }
          //  cyclicBarrier.await();
            START.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        try {
            STOP.await();
          //  cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
