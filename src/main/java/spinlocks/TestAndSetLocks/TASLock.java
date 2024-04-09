package spinlocks.TestAndSetLocks;
import java.util.concurrent.atomic.AtomicBoolean;

import spinlocks.Lock;

public class TASLock extends Lock {

    AtomicBoolean state = new AtomicBoolean(false);

    public TASLock() {
    }

    @Override
    public void lock() {
        while (state.getAndSet(true)) {
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }
}
