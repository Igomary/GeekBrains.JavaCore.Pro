package lesson4;

public class MyApp {
    private final Object monitor = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {
        MyApp thisInstance = new MyApp();
        new Thread(() -> thisInstance.print('A')).start();
        new Thread(() -> thisInstance.print('B')).start();
        new Thread(() -> thisInstance.print('C')).start();

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
