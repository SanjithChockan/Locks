package spinlocks.ClassicLocks;
import java.util.concurrent.atomic.AtomicInteger;

import spinlocks.Lock;

public class BakeryLockV2 extends Lock {

    private int n;
    private int offset = 0;
    private AtomicInteger[] choosing;
    private AtomicInteger[] number;

    public BakeryLockV2(int n) {
        this.n = n;
        choosing = new AtomicInteger[n];
        number = new AtomicInteger[n];

        for (int i = 0; i < n; i++) {
            choosing[i] = new AtomicInteger(0);
            number[i] = new AtomicInteger(0);
        }
    }

    @Override
    public void lock() {
        int i = ((int) Thread.currentThread().getId()) - offset;
        choosing[i].set(1);
        number[i].set(getMaxNumber() + 1);
        choosing[i].set(0);

        for (int j = 0; j < n; j++) {
            // wait till all participating threads choose a number
            if (i != j) {
                boolean wait = true;
                while (wait) {
                    wait = choosing[j].get() != 0;
                }

                wait = true;
                while (wait) {
                    wait = (number[j].get() != 0
                            && ((number[j].get() < number[i].get())
                                    || ((number[j].get() == number[i].get()) && j < i)));
                }
            }
        }
    }

    @Override
    public void unlock() {
        int i = ((int) Thread.currentThread().getId()) - offset;
        number[i].set(0);
    }

    private int getMaxNumber() {
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int currentValue = number[i].get();
            maxValue = Math.max(maxValue, currentValue);
        }
        return maxValue;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
