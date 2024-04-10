package spinlocks.QueueLocks;

public class QNode {
    volatile boolean locked;
    volatile QNode next;

    public QNode() {
        locked = false;
        next = null;
    }
}
