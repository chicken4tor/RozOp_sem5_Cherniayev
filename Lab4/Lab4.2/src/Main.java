import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.StampedLock;

class gardener extends Thread {
    public gardener(garden parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            Random rn = new Random();
            int i = rn.nextInt(parent.getSize());
            int j = rn.nextInt(parent.getSize());
            this.parent.setValue(i, j, garden.stateOfPlant.BLOOMING);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Gardener died");
                this.interrupt();
            }
        }
    }

    private final garden parent;
}

class nature extends Thread {
    public nature(garden parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            Random rn = new Random();
            int i = rn.nextInt(parent.getSize());
            int j = rn.nextInt(parent.getSize());
            int n = rn.nextInt(3);

            switch (n) {
                case (0):
                    this.parent.setValue(i, j, garden.stateOfPlant.BLOOMING);
                    break;
                case (1):
                    this.parent.setValue(i, j, garden.stateOfPlant.GROWING);
                    break;
                default:
                    this.parent.setValue(i, j, garden.stateOfPlant.ROTTING);
                    break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Nature died");
                this.interrupt();
            }
        }
    }
    private final garden parent;
}

class monitor extends Thread {
    public monitor(garden parent, int id) {

        this.parent = parent;
        this.id = id;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {

            if (id == 1)
                parent.readToFile();
            else parent.showGarden();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Monitor died");
                this.interrupt();

            }

        }


    }

    private final int id;
    private final garden parent;
}

class garden {
    public garden(int size) {
        this.size = size;
        vegetation = new int[this.size][this.size];
        lock = new StampedLock();

    }

    public void setValue(int i, int j, stateOfPlant value) {
        System.out.println("Waiting to set value, id: " + Thread.currentThread().getId());
        long stamp = lock.writeLock();
        if (i < 0 || j < 0 || i >= size || j >= size)
            throw new IllegalArgumentException("Out of bounds");
        vegetation[i][j] = value.getValue();
        System.out.println("Set value, id: " + Thread.currentThread().getId());
        lock.unlockWrite(stamp);
    }

    public void readToFile() {
        System.out.println("Waiting to read value, id: " + Thread.currentThread().getId());
        long stamp = lock.readLock();
        System.out.println("Reading to file, id: " + Thread.currentThread().getId());
        RandomAccessFile stream = null;
        try {
            stream = new RandomAccessFile("C:\\Users\\Admin\\IdeaProjects\\lab1\\src\\Lab4\\mainb.txt", "rw");


            stream.writeBytes("-----------\n");
            stream.seek(stream.length());
            for (int i = 0; i < size; i++) {
                stream.writeBytes(Arrays.toString(vegetation[i]) + "\n");
            }


            stream.writeBytes("-----------\n");
        } catch (IOException e) {

        }

        lock.unlockRead(stamp);
    }

    public void showGarden() {
        long stamp = lock.readLock();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%5d ", vegetation[i][j]);
            }
            System.out.println();
        }
        lock.unlockRead(stamp);
    }

    public int getSize() {
        return this.size;
    }

    public enum stateOfPlant {
        GROWING(0), BLOOMING(1), ROTTING(2);
        private final int value;

        private stateOfPlant(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private final int size;
    private final StampedLock lock;

    private final int[][] vegetation;
}

public class Main {
    public static void main(String[] args) {
        garden grdn = new garden(5);
        Thread grdner = new gardener(grdn);
        Thread ntr = new nature(grdn);
        Thread mon1 = new monitor(grdn, 1);
        Thread mon2 = new monitor(grdn, 2);
        grdner.start();
        ntr.start();
        mon1.start();
        mon2.start();
    }
}