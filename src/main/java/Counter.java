import spinlocks.Lock;

public class Counter {
    
    long count;
    Lock lock;

    public Counter(Lock lock) {
        this.lock = lock;
        this.count = 0;
    }

    public void increment() {
        lock.lock();
        count++;
        displayCount();
        lock.unlock();
    }

    public void displayCount() {
        System.out.println(count);
    }
}
