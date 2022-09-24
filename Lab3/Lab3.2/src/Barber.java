import java.util.concurrent.Semaphore;

public class Barber implements Runnable {
    BarberSHop shop;
    Semaphore semaphore;
    public Barber(BarberSHop shop, Semaphore semaphore) {
        this.shop = shop;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while(true) {
            try {
                if (shop.customersCount() == 0 && !shop.getIsSleeping()) {
                    System.out.println("Перукар спить");
                    shop.setIsSleeping(true);
                }
                Visitor currentVisitor = shop.takeCustomer();
                System.out.println("Перукар стриже " + (currentVisitor.getId()+1));
                Thread.currentThread().sleep(30);
                System.out.println("Перукар закінчив стригти клієнта " + (currentVisitor.getId()+1) + " тепер іде будити наступного");

                currentVisitor.setId(currentVisitor.getID());

                synchronized (currentVisitor) {
                    currentVisitor.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
