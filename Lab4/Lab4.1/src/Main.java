//Черняєв Андрій ІПС-32

public class Main {
    public static void main(String[] args) {
        File file = new File();
        WorkingThread wt1 = new WorkingThread(file, 1);
        WorkingThread wt2 = new WorkingThread(file, 2);
        WorkingThread wt3 = new WorkingThread(file, 3);

        wt1.setDaemon(true);
        wt2.setDaemon(true);
        wt3.setDaemon(true);

        wt1.start();
        wt2.start();
        wt3.start();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}