package lesson5;



import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


public class MainClass {
    public static final int CARS_COUNT = 4;
    private static final CountDownLatch START = new CountDownLatch(CARS_COUNT);
    private static final CountDownLatch STOP = new CountDownLatch(CARS_COUNT);
    //private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);


    public static void main(String[] args) {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10),START, STOP);
        }

        for (int i = 0; i < cars.length; i++) {
            final int w = i;
            new Thread(() -> {
                // while (START.getCount() > 0) {
                try {
                    cars[w].run();
                    START.await();
                    //cyclicBarrier.await();
                    cars[w].start();
                   STOP.await();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                //  }
            }).start();
        }
        try{
            START.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        try {
            STOP.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
