import java.security.SecureRandom;
import java.util.concurrent.Semaphore;

public class task2Java {
    private static final Semaphore semaphore = new Semaphore(8);

    public static void main(String[] args){
        for(int i = 0; i < 10; i++){
            Thread t = new Thread(new Reader(String.valueOf(i)));
            t.start();
        }
    }

    private static class Reader implements Runnable {
        private final String name;
        Reader(String name){
            this.name = name;
        }

        public String getName() {
            return "Reader" + name;
        }

        @Override
        public void run() {
            System.out.println(getName() + ": standing in queue");
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            var random = new SecureRandom();
            int onHands = random.nextInt(2);
            if(onHands == 1) {
                System.out.println(getName() + " take book to home");
            } else {
                System.out.println(getName() + ": take book to reading space");
            }


            try {
                int timeout = 1000;
                Thread.sleep((long) timeout * (onHands + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(getName() + ": book is returned");
            semaphore.release();

        }
    }
}