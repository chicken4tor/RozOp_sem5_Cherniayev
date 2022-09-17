//прапорщики Іванов, Петров і Нечипорчук займаються розкраданням військового
//майна зі складу рідної військової частини.
//прапорщики ввели поділ праці: Іванов виносить майно зі складу, Петров вантажить його в вантажівку,
//а Нечипорчук підраховує вартість майна. Потрібно скласти багатопоточний додаток, що моделює діяльність прапорщиків.
//При вирішенні використати парадигму «виробник-споживач» з активним очікуванням.

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args)
            throws InterruptedException {
        final Thing thing = new Thing();
        thing.generateStorage();

        Thread ivanov = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thing.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread petrov = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thing.produce_consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread nechyporenko = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thing.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        ivanov.start();
        petrov.start();
        nechyporenko.start();

        ivanov.join();
        petrov.join();
        nechyporenko.join();
    }

    public static class Thing {
        private final int numberOfThings = 5;
        private int totalPrice = 0;

        private final BlockingQueue<Thing> storage = new ArrayBlockingQueue<Thing>(numberOfThings);
        private final BlockingQueue<Thing> ivanovToPetrov = new ArrayBlockingQueue<Thing>(numberOfThings);
        private final BlockingQueue<Thing> truck = new ArrayBlockingQueue<Thing>(numberOfThings);
        int capacity = 1;
        int price;
        Random rand = new Random();

        Thing() {
            this.price = rand.nextInt(1000);
        }

        public void generateStorage() throws InterruptedException {
            for (int i = 0; i < numberOfThings; i++) {
                storage.put(new Thing());
            }
        }

        public int GetPrice() {
            return price;
        }

        public void produce() throws InterruptedException {
            int value = 0;
            while (true) {
                synchronized (this) {
                    while (ivanovToPetrov.size() == capacity) {
                        wait();
                    }
                    System.out.println("Іванов вкрав");
                    ivanovToPetrov.put(storage.take());

                    notify();

                    Thread.sleep(1000);

                    if (storage.isEmpty()) {
                        break;
                    }
                }
            }
        }

        public void produce_consume() throws InterruptedException {
            int value = 0;
            while (true) {
                synchronized (this) {
                    while (ivanovToPetrov.size() == 0) {
                        wait();
                    }
                    System.out.println("Петров погрузив");
                    truck.put(ivanovToPetrov.take());

                    notify();

                    Thread.sleep(1000);

                    if (storage.isEmpty()) {
                        break;
                    }
                }
            }
        }

        public void consume() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    while (truck.isEmpty()) {
                        wait();
                    }
                    totalPrice += truck.take().GetPrice();
                    System.out.println("Нечипоренко підраховав на сумму: " + totalPrice);

                    notify();

                    Thread.sleep(1000);
                }
            }
        }
    }
}