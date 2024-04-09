package spinlocks.QueueLocks;

public class QNode {
    volatile boolean locked;
    QNode next;

    public QNode() {
        locked = false;
        next = null;
    }
}
