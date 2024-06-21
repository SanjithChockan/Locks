package spinlocks.QueueLocks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class QNode {
    // for debugging
    AtomicInteger id;

    // timestamp
    AtomicInteger timestamp;
    
    volatile boolean locked;
    // Used for MCSLock 
    volatile QNode next;
    
    // Used for MCSOptimizedRelease
    //volatile boolean isReleasing;
    AtomicBoolean isReleasing;
    

    // Used for hemlock
    volatile AtomicReference<QNode> grant;

    public QNode() {
        locked = false;
        next = null;
        isReleasing = new AtomicBoolean(false);
        timestamp = new AtomicInteger(0);
    }

    public QNode(int id) {
        locked = false;
        next = null;
        isReleasing = new AtomicBoolean(false);
        this.id = new AtomicInteger(id);
        timestamp = new AtomicInteger(0);
    }
}