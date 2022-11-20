public class MyThread extends Thread {
    private final MilOrd order;
    private final int begin;
    private final int end;
    private final CyclicBarrierCustom barrier;
    private final int num;
    MyThread(MilOrd order,int begin,int end, CyclicBarrierCustom barrier,int num) {
        this.order = order;
        this.begin = begin;
        this.end = end;
        this.barrier=barrier;
        this.num = num;
    }
    public void run() {
        while (true){
            try {
                if(order.isStopExecuting()){
                    System.out.println(getClass().getSimpleName() + num + " закінчив свою роботу. Умова зупинки виконана");
                    break;
                }
                System.out.println(getClass().getSimpleName()+ num +" виконав роботу ");
                order.work(begin,end);
                Thread.sleep(10);
                barrier.await();
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }
}