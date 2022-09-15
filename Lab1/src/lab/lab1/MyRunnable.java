package lab.lab1;

import javax.swing.*;

public class MyRunnable implements Runnable {
    private int target;
    private JSlider slider;

    private static boolean exit;

    public MyRunnable(int target, JSlider slider) {
        this.slider = slider;
        this.target = target;
    }

    @Override
    public void run() {
        if (Window.semaphore.compareAndSet(0, 1)) {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (slider) {
                    while (slider.getValue() != target) {
                        slider.setValue(target);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            Window.semaphore.set(0);
        }
    }
}
