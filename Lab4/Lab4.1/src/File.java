
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class File {
    public ArrayList<Record> records;
    private ReadWriteLock lock;

    public File() {
        records = InitializeStartRecords();
        lock = new ReentrantReadWriteLock();
    }
    private ArrayList<Record> InitializeStartRecords() {
        ArrayList<Record> res = new ArrayList<>();
        res.add(new Record("Dima", "Tkachenko", "Sasha", "001"));
        res.add(new Record("Sasha", "Shevchenko", "Petro", "010"));
        res.add(new Record("Petro", "Franko", "Ivan", "011"));
        res.add(new Record("Ivan", "Stus", "Dima", "100"));
        return res;
    }

    public void LockRead() {
        lock.readLock().lock();
    }
    public void UnlockRead() {
        lock.readLock().unlock();
    }
    public void LockWrite() {
        lock.writeLock().lock();
    }
    public void UnlockWrite() {
        lock.writeLock().unlock();
    }
}