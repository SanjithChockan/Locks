package spinlocks.ClassicLocks;
import java.util.concurrent.atomic.AtomicIntegerArray;

import spinlocks.Lock;

public class FilterLock extends Lock {

    private int n;
    private int offset = 0;
    private AtomicIntegerArray level;
    private AtomicIntegerArray victim;

    public FilterLock(int n) {
        this.n = n;
        level = new AtomicIntegerArray(n);
        victim = new AtomicIntegerArray(n);
    }

    @Override
    public void lock() {
        int me = ((int) Thread.currentThread().getId()) - offset;

        for (int i = 1; i < n; i++) {
            // enter into level and let other threads go
            level.set(me, i);
            victim.set(i, me);

            // wait until either a threads finishes C.S or another thread enters into
            // current thread level
            boolean wait = true;
            while (wait) {
                wait = false;
                for (int k = 0; k < n; k++) {
                    if (k != me) {
                        if (level.get(k) >= i && victim.get(i) == me) {
                            wait = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void unlock() {
        int me = ((int) Thread.currentThread().getId()) - offset;
        level.set(me, 0);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
