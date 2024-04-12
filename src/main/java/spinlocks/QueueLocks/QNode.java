package spinlocks.QueueLocks;

import java.util.concurrent.atomic.AtomicReference;

public class QNode {
    volatile boolean locked;
    // Used for MCSLock 
    volatile QNode next;

    // Used for hemlock
    volatile AtomicReference<QNode> grant;

    public QNode() {
        locked = false;
        next = null;
    }
}
