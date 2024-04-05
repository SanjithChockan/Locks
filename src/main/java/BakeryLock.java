import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class BakeryLock extends Lock {

    private int n;
    private int offset = 0;
    // volatile boolean[] flag;
    private AtomicBoolean[] flag;
    // int[] label;
    private AtomicIntegerArray label;

    public BakeryLock(int n) {
        this.n = n;
        label = new AtomicIntegerArray(n);
        flag = new AtomicBoolean[n];
        for (int i = 0; i < n; i++) {
            flag[i] = new AtomicBoolean(false);
        }
    }

    @Override
    public void lock() {
        int i = ((int) Thread.currentThread().getId()) - offset;
        flag[i].set(true);

        // need to write function to get max value from array
        label.set(i, getMaxLabel() + 1);

        boolean wait = true;
        while (wait) {
            wait = false;
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    if (flag[k].get() == true
                            && ((label.get(k) < label.get(i)) || (label.get(k) == label.get(i) && k < i))) {
                        wait = true;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void unlock() {
        int i = ((int) Thread.currentThread().getId()) - offset;
        flag[i].set(false);
    }

    private int getMaxLabel() {
        // iterate through labels array and return max value
        int maxValue = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            // get current value
            int currentValue = label.get(i);
            // set max value to current value if currentValue > maxValue
            if (currentValue > maxValue) {
                maxValue = currentValue;
            }
        }
        return maxValue;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
