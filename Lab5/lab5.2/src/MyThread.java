import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread extends Thread {
    CyclicBarrier cyclicbar;
    char num;
    Str myClass;
    MyThread(CyclicBarrier cyclicbar, char num, Str myClass) {
        this.cyclicbar = cyclicbar;
        this.num = num;
        this.myClass = myClass;
    }
    public void run() {
        while(true){
            try {
                if(myClass.getStopExecutingStatus()){
                    System.out.println(getClass().getSimpleName()+ " " + num + " закінчив свою роботу. Умова зупинки виконана");
                    break;
                }
                System.out.println(getClass().getSimpleName()+ " " + num + " виконав роботу");
                myClass.work(num);
                sleep(100);
                cyclicbar.await();
            } catch (BrokenBarrierException | InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }
}