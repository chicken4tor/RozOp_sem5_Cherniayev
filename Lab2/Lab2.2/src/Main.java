//прапорщики Іванов, Петров і Нечипорчук займаються розкраданням військового
//майна зі складу рідної військової частини.
//прапорщики ввели поділ праці: Іванов виносить майно зі складу, Петров вантажить його в вантажівку,
//а Нечипорчук підраховує вартість майна. Потрібно скласти багатопоточний додаток, що моделює діяльність прапорщиків.
//При вирішенні використати парадигму «виробник-споживач» з активним очікуванням.

import java.util.LinkedList;
import static java.lang.Math.random;

public class Main {
    public static void main(String[] args)
            throws InterruptedException
    {
        final Thing thing = new Thing();

        Thread ivanov = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    thing.produce();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ІВАНОВ ВКРАВ");
            }
        });

        Thread petrov = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    thing.consume();
                }
                catch (InterruptedException e) {
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
        //nechyporenko.start();

        ivanov.join();
        petrov.join();
        //nechyporenko.join();
    }

    public static class Thing {
        LinkedList<Integer> list = new LinkedList<>();
        int capacity = 1;

        public void produce() throws InterruptedException
        {
            int value = 0;
            while (true) {
                synchronized (this)
                {
                    while (list.size() == capacity) {
                        wait();
                    }
                    System.out.println("Іванов вкрав - " + value);

                    list.add(value++);

                    notify();

                    Thread.sleep(1000);
                }
            }
        }

        public void consume() throws InterruptedException
        {
            while (true) {
                synchronized (this)
                {
                    while (list.size() == 0){
                        wait();
                    }
                    int val = list.removeFirst();
                    System.out.println("Петров погрузив-" + val);
                    notify();

                    // and sleep
                    Thread.sleep(1000);
                }
            }
        }
    }
}