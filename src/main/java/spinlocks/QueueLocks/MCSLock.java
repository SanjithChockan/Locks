package spinlocks.QueueLocks;

import java.util.concurrent.atomic.AtomicReference;

import spinlocks.Lock;

public class MCSLock extends Lock{
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myNode;

    public MCSLock() {
        tail = new AtomicReference<QNode>(null);
        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }

    @Override
    public void lock() {
        QNode qnode = myNode.get();
        QNode pred = tail.getAndSet(qnode);

        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;
            // wait until pred gives up lock
            while (qnode.locked) {}
        }
    }

    @Override
    public void unlock() {
        QNode qnode = myNode.get();
        if (qnode.next == null) {
            if (tail.compareAndSet(qnode, null)) {
                return;
            }
            // wait until pred fills in its next field
            while (qnode.next == null) {}
        }
        qnode.next.locked = false;
        qnode.next = null;
    }
}
