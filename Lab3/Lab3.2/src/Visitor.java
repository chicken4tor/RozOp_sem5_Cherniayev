import java.util.concurrent.Semaphore;

public class Visitor implements Runnable{
    private final BarberSHop shop;
    private Semaphore semaphore;
    private int id;
    private static int ID = 0;

    public void setId(int ID) {
        this.id = ID;
        this.ID++;
    }
    public static int getID(){
        return ID;
    }

    public int getId() {
        return id;
    }

    public Visitor(BarberSHop shop, Semaphore semaphore) {
        this.shop = shop;
        this.semaphore = semaphore;
        this.setId(getID());
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.currentThread().sleep(((getId()%3)+1)*1);
                semaphore.acquire();
                Thread.currentThread().sleep(((getId()%3)+1)*30);
                int currentCount = shop.customersCount();

                if (!shop.getIsSleeping()) {
                    System.out.println("Клієнт " + (id+1) + " ліг спати. Він " + (currentCount + 1) + " у черзі");
                } else if (shop.getIsSleeping()) {
                    System.out.println("Клієнт " + (id+1) + " розбудив перукаря. Він " + (currentCount + 1));
                    shop.setIsSleeping(false);
                }
                shop.addCustomer(this);
                semaphore.release();
                synchronized (this) {
                    wait();
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
