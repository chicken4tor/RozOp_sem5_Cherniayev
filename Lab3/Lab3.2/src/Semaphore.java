public class Semaphore {
    private boolean isAvailable = true;
    public void acquire() throws InterruptedException{
        this.isAvailable=false;
    }

    public void release(){
        this.isAvailable=true;
    }

    public boolean getStatus(){
        return isAvailable;
    }
}
