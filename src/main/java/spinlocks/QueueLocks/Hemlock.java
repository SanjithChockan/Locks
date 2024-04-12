package spinlocks.QueueLocks;
import java.util.concurrent.atomic.AtomicReference;

import spinlocks.Lock;

public class Hemlock extends Lock{

    ThreadLocal<QNode> myNode;
    AtomicReference<QNode> tail;

    public Hemlock() {
        tail = new AtomicReference<QNode>(null);

        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }

    public void lock() {
        QNode qnode = myNode.get();
        QNode pred = tail.getAndSet(qnode);

        if (pred != null) {
            // contention: busy wait
            while (pred.grant != tail) {};
            pred.grant = null;
        }
    }

    public void unlock() {
        QNode qnode = myNode.get();
        if (!tail.compareAndSet(qnode, null)) {
            // waiters exist
            qnode.grant = tail;
            while(qnode.grant != null){};
        }
    }
}
