import java.util.concurrent.atomic.AtomicBoolean;

public class TTASLock extends Lock{

    AtomicBoolean state = new AtomicBoolean(false);
    public TTASLock() {
    }

    @Override
    public void lock() {
        while (true) {
            while (state.get()) {};
            if (!state.getAndSet(true))
                return;
        }

    }

    @Override
    public void unlock() {
        state.set(false);
    }
    
}
