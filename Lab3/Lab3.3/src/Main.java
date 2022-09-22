import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

class CPot {
    private int m_MaxSips;
    private Integer m_CurrentSips;

    public CPot(int _MaxSips) {
        m_MaxSips = _MaxSips;
        m_CurrentSips = 0;
    }

    boolean CanAddSip() {
        return m_CurrentSips < m_MaxSips;
    }

    void AddSip() {
        ++m_CurrentSips;
    }

    void EatAll() {
        m_CurrentSips = 0;
    }
}

class CBee extends Thread {

    @Override
    public void run() {
        while (!interrupted()) {
            synchronized (Main.Pot) {
                if (Main.Pot.CanAddSip()) {
                    System.out.println("Бджола:  Додала Ковточок!");
                    Main.Pot.AddSip();
                }
                else if (Main.Bear.IsSleeping()) {
                    System.out.println("Бджола:  Не можу додати ковточок. Ведмідь прокидайся!");
                    Main.Bear.WakeUp();
                }
            }
        }
    }
}

class CBear extends Thread {

    private AtomicBoolean m_IsSleeping = new AtomicBoolean(true);

    @Override
    public void run() {
        while (!interrupted()) {
            if (!m_IsSleeping.get()) {
                synchronized (Main.Pot) {
                    System.out.println("Ведмідь: Я прокинувся і їм мед!");
                    Main.Pot.EatAll();
                    m_IsSleeping.set(true);
                }
            }
        }
    }

    public void WakeUp() {
        m_IsSleeping.set(false);
    }

    public boolean IsSleeping() {
        return m_IsSleeping.get();
    }

}

public class Main {

    static ArrayList<CBee> Bees;
    static CBear Bear;
    static CPot Pot;

    public static void main(String[] args) {

        Pot = new CPot(5);
        Bear = new CBear();
        Bear.start();
        Bees = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            Bees.add(new CBee());
            Bees.get(i).start();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        for (CBee Bee : Bees) {
            Bee.interrupt();
        }
        Bear.interrupt();
    }
}