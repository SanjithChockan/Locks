import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class FilterLockV2 extends Lock {
    int n;
    private int offset = 0;
    AtomicIntegerArray victim;
    AtomicBoolean[] flag;
    ConcurrentHashMap<Integer, AtomicBoolean[]> levels = new ConcurrentHashMap<Integer, AtomicBoolean[]>();

    public FilterLockV2(int n) {
        System.out.println("Filter Lock V2: Blackbox Version (Derivation from Peterson)");
        this.n = n;

        victim = new AtomicIntegerArray(n);
        // no victimes at any level at beginning, so set to -1 as initial value
        for (int i = 0; i < n; i++) {
            victim.set(i, -1);
        }

        // flag to indicate interest or inside C.S
        flag = new AtomicBoolean[n];
        for (int i = 0; i < n; i++) {
            flag[i] = new AtomicBoolean(false);
        }

        // levels keep track of threads that visited the level
        for (int i = 0; i < n - 1; i++) {
            AtomicBoolean[] visited = new AtomicBoolean[n];
            for (int j = 0; j < n; j++) {
                visited[j] = new AtomicBoolean(false);
            }
            levels.put(i, visited);
        }
    }

    @Override
    public void lock() {

        int me = ((int) Thread.currentThread().getId()) - offset;
        flag[me].set(true);
        // Loop through all levels
        for (int l = 0; l < n - 1; l++) {
            levels.get(l)[me].set(true);
            victim.set(l, me);

            boolean wait = true;
            while (wait) {
                wait = false;
                // if at least one flag in the current level is still true and me is victim,
                // then loop
                for (int i = 0; i < n; i++) {
                    if (i != me) {
                        if ((flag[i].get() && levels.get(l)[i].get()) && victim.get(l) == me) {
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
        for (int i = n - 2; i >= 0; i--) {
            levels.get(i)[me].set(false);
        }
        flag[me].set(false);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
