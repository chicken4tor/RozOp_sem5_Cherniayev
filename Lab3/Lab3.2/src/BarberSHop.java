import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class BarberSHop {

    private BlockingQueue<Visitor> visitors;
    private Thread[] threads;
    private Thread barber;
    private int size;
    private boolean isSleeping = false;
    private Semaphore semaphore;

    public boolean getIsSleeping(){
        return isSleeping;
    }
    public void setIsSleeping(boolean val){
        isSleeping = val;
    }

    BarberSHop(int n) {
        this.semaphore=new Semaphore(1);
        this.visitors = new LinkedBlockingQueue<>();
        this.size=n;
        threads = new Thread[size];
        for(int i = 0; i < size; i++) {
            threads[i] = new Thread(new Visitor(this, semaphore));
        }
        barber = new Thread(new Barber(this, semaphore));
    }

    public void start() {
        for(int i = 0; i < size; i++) {
            threads[i].start();
        }
        barber.start();
    }

    public void addCustomer(Visitor customer) throws InterruptedException {
        visitors.put(customer);
    }

    public Visitor takeCustomer() throws InterruptedException {
        return visitors.take();
    }

    public int customersCount() {
        return visitors.size();
    }
}
