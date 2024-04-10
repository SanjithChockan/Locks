package spinlocks.QueueLocks;

import java.util.concurrent.atomic.AtomicReference;

import spinlocks.Lock;

public class Hemlock extends Lock{
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myGrant;

    public Hemlock() {
        tail = null;
        myGrant = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return null;
            }
        };
    }

    public void lock() {
        QNode grant = myGrant.get();
        QNode pred = tail.getAndSet(grant);

        if (pred != null) {
            // contention : must wait
            while (pred != tail.get()) {};
            pred = null;
        }
    }

    public void unlock() {
        QNode grant = myGrant.get();
        if(!tail.compareAndSet(grant, null)) {
            // one or more waiters
            grant = tail.get();
            while (grant != null) {};
        }
    }
}
