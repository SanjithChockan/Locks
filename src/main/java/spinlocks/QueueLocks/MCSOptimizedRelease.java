package spinlocks.QueueLocks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import spinlocks.Lock;

public class MCSOptimizedRelease extends Lock {
    AtomicInteger id = new AtomicInteger(0);
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myNode;

    public MCSOptimizedRelease() {
        tail = new AtomicReference<QNode>(null);
        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode(id.getAndIncrement());
            }
        };
    }

    @Override
    public void lock() {
        QNode qnode = myNode.get();

        // can spin on isReleasing until successor thread sets it to false if it isn't
        // already
        if (qnode.isReleasing.get()) {
            // arrive here if exited release method by flagging
            // should be unlocked in acquire method by successor
            while (qnode.isReleasing.get()) {
            }
        }

        QNode pred = tail.getAndSet(qnode);
        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;
            // Next problem: need to gaurantee execution of if statement and block to remove spinning above
            if (pred.isReleasing.get()) {
                // set pred.next to null in case it exits by flag and doesn't reset its next
                // should be done before resetting isReleasing
                // value and reset for thread to exit infinite loop
                pred.next = null;
                pred.isReleasing.set(false);
                // System.out.println("Thread " + qnode.id.get() + ": Enter by flag by
                // predecessor Thread " + pred.id.get());
                qnode.locked = false;
                return;
            }
            // wait until pred gives up lock
            while (qnode.locked) {
            }
        }

    }

    @Override
    public void unlock() {
        QNode qnode = myNode.get();
        qnode.isReleasing.set(true);;

        if (qnode.next == null) {
            // check if no contenders exist to replace tail with null
            if (tail.compareAndSet(qnode, null)) {
                // will be stuck in infinite loop if no one in queue and isReleasing is not
                // reset
                qnode.isReleasing.set(false);
                // System.out.println("Thread " + qnode.id.get() + ": No contenders. Released
                // lock and reset tail");
                return;
            }

            // case: there is a process enqueuing and current process exits by flag
            // ideally, successor should set isReleasing to False in acquire method

            // thread releasing lock exits release with isReleasing = True

            // System.out.println(qnode + " Thread " + qnode.id.get() + ": release lock by
            // flag");

        } else {
            // System.out.println("Thread " + qnode.id.get() + ": setting successor Thread
            // "+ qnode.next.id.get() + " locked field to false");
            qnode.next.locked = false;
            qnode.next = null;
            qnode.isReleasing.set(false);
        }

    }
}