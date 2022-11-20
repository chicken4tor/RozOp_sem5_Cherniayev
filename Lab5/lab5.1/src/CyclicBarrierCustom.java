public class CyclicBarrierCustom {
    int initialParties;
    int partiesAwait;
    Runnable cyclicBarrierEvent;

    public CyclicBarrierCustom(int parties, Runnable cyclicBarrierEvent) {
        initialParties=parties;
        partiesAwait=parties;
        this.cyclicBarrierEvent = cyclicBarrierEvent;
    }
    public synchronized void await() throws InterruptedException {
        partiesAwait--;
        if (partiesAwait > 0) {
            this.wait();
        } else {
            partiesAwait = initialParties;
            notifyAll();
            cyclicBarrierEvent.run();
        }
    }
}