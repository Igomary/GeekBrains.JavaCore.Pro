package lesson4;

public class MyApp {
    private final Object monitor = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {
        MyApp thisInstance = new MyApp();
        Thread t1 = new Thread(() -> thisInstance.print('A'));
        Thread t2 = new Thread(() -> thisInstance.print('B'));
        Thread t3 = new Thread(() -> thisInstance.print('C'));
        t1.start();
        t2.start();
        t3.start();

    }

    private void print(char letter) {
        synchronized (monitor) {
            try{
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != letter) {
                        monitor.wait();
                    }
                    System.out.print(letter);
                    currentLetter = letter != 'A' ? letter == 'B' ? 'C' : 'A' : 'B';
                    monitor.notifyAll();
                }
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
}
