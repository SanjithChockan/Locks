import java.util.concurrent.atomic.AtomicBoolean;

public class TASLock extends Lock {

    AtomicBoolean state = new AtomicBoolean(false);

    public TASLock() {
        System.out.println("TAS Lock");
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
