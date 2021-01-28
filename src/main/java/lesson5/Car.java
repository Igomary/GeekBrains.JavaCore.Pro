package lesson5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
//import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private CountDownLatch cStart;
    private CountDownLatch cStop;
   // private CyclicBarrier cyclicBarrier;
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }

    public CountDownLatch getcStop() {
        return cStop;
    }

    public Car(Race race, int speed, CountDownLatch cStart, CountDownLatch cStop) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cStart = cStart;
        this.cStop = cStop;
       // this.cyclicBarrier = cyclicBarrier;
    }

    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
           // cyclicBarrier.await();
            cStart.countDown();
            cStart.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

            start();

    }

    private void start() {
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        cStop.countDown();
        // cyclicBarrier.await();
    }


}