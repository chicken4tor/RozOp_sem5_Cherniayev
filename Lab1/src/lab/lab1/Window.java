package lab.lab1;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

public class Window implements ActionListener, ChangeListener {
    final private JFrame frame;
    final private JSlider slider;
    private JButton button, button1, button2, buttonstop1, buttonstop2;
    final private JSpinner spinner0;
    final private JSpinner spinner1;
    private Thread th1, th2;

    public static AtomicInteger semaphore = new AtomicInteger();

    Window(String name, int width, int height) {
        frame = new JFrame(name);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        slider = new JSlider(0, 100, 50);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(10);
        slider.setPaintLabels(true);
        slider.setBounds(20, 20, 350, 50);
        frame.add(slider);

        button = new JButton("Start!");
        button.setBounds(150, 250, 100, 50);
        button.addActionListener(this);
        frame.add(button);

        button1 = new JButton("Start1");
        button1.setBounds(50, 300, 100, 50);
        button1.addActionListener(this);
        frame.add(button1);

        button2 = new JButton("Start2");
        button2.setBounds(250, 300, 100, 50);
        button2.addActionListener(this);
        frame.add(button2);

        buttonstop1 = new JButton("Stop1");
        buttonstop1.setBounds(50, 400, 100, 50);
        buttonstop1.addActionListener(this);
        frame.add(buttonstop1);

        buttonstop2 = new JButton("Stop2");
        buttonstop2.setBounds(250, 400, 100, 50);
        buttonstop2.addActionListener(this);
        frame.add(buttonstop2);

        spinner0 = new JSpinner();
        spinner0.setModel(new SpinnerNumberModel(1, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1));
        spinner0.setValue(1);
        spinner0.setBounds(80, 125, 60, 60);
        spinner0.addChangeListener(this);

        spinner1 = new JSpinner();
        spinner1.setModel(new SpinnerNumberModel(1, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1));
        spinner1.setValue(1);
        spinner1.setBounds(260, 125, 60, 60);
        spinner1.addChangeListener(this);

        frame.add(spinner0);
        frame.add(spinner1);

        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            sas();
        } else if (e.getSource() == button1) {
            if (th1 != null) {
                if (th1.isAlive()) {
                    th1.interrupt();
                }
            } else {
                th1 = new Thread(new MyRunnable(10, slider));
                th1.setPriority(Thread.MIN_PRIORITY);
                th1.start();
            }
        } else if (e.getSource() == button2) {
            if (th2 != null) {
                if (th2.isAlive()) {
                    th2.interrupt();
                }
            } else {
                th2 = new Thread(new MyRunnable(90, slider));
                th2.setPriority(Thread.MAX_PRIORITY);
                th2.start();
            }

        } else if (e.getSource() == buttonstop1) {
            if (th1 != null) {
                if (th1.isAlive()) {
                    th1.interrupt();
                }
            }
        } else if (e.getSource() == buttonstop2) {
            if (th2 != null) {
                if (th2.isAlive()) {
                    th2.interrupt();
                }
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spinner0) {
            th1.setPriority((int) spinner0.getValue());
        } else if (e.getSource() == spinner1) {
            th2.setPriority((int) spinner1.getValue());
        }
    }

    private void sas() {
        th1 = new LabThread(slider, 10);
        th2 = new LabThread(slider, 90);
        th1.setPriority((int) spinner0.getValue());
        th2.setPriority((int) spinner1.getValue());
        th1.start();
        th2.start();
    }
}

class LabThread extends Thread {

    private final JSlider slider;
    private int target;

    LabThread(JSlider slider, int target) {
        this.slider = slider;
        this.target = target;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (slider) {
                int sliderValue = slider.getValue();
                if (target != sliderValue) {
                    slider.setValue(target);
                }
            }
            try {
                LabThread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}