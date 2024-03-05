import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    static Counter c;
    static FileWriter myWriter;
    static BufferedWriter outputWriter;
    //static OutputStreamWriter outputWriter;

    public static void main(String[] args) throws Exception {
        // Driver Code to invoke locks

        int limit = 1000000;
        String lockType = args[0];
        int num = Integer.parseInt(args[1]);
        boolean toAppend = "true".equals(args[2]);

        String filePath = "/Users/sanjith/Computer Science/multicore/Locks/src/resources/data/";
        try {
            myWriter = new FileWriter(filePath + lockType + "/" + lockType + "-" + num + ".txt", toAppend);
            outputWriter = new BufferedWriter(myWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lockType.equals("Tournament")) {
            invokeTournamentAlgorithm(num, limit);
            outputWriter.close();
        } else if (lockType.equals("FilterV1")) {
            invokeFilterLock(num, limit, 1);
            outputWriter.close();
        } else if (lockType.equals("FilterV2")) {
            invokeFilterLock(num, limit, 2);
            outputWriter.close();
        } else if (lockType.equals("BakeryLockV1")) {
            invokeBakeryLock(num, limit, 1);
            outputWriter.close();
        } else if (lockType.equals("BakeryLockV2")) {
            invokeBakeryLock(num, limit, 2);
            outputWriter.close();
        }
    }

    static void invokeTASLock(int n, int limit) throws Exception{
        
    }

    static void invokeTTASLock(int n, int limit) throws Exception{
        
    }

    static void invokeTournamentAlgorithm(int n, int limit) throws Exception {
        Thread[] threads = new Thread[n];
        TournamentTree tt = new TournamentTree(n);
        c = new Counter(tt);
        // spawn n threads
        for (int i = 0; i < n; i++) {
            // each thread invokes incrementcounter "limit" times
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < limit; j++) {
                        c.increment();
                    }
                }
            });

            // instantiate lock object with offset to correctly index threads from 0 to n-1
            if (i == 0) {
                tt.setOffset((int) threads[i].getId());
            }
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            threads[i].start();
        }

        // Wait for each thread to complete using the join() method
        for (Thread thread : threads) {
            try {
                thread.join(); // Main thread waits for each thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long stopTime = System.currentTimeMillis();
        String outputData = String.valueOf((stopTime - startTime));
        outputWriter.write(outputData);
        outputWriter.flush();
        outputWriter.newLine();
        // display count
        c.displayCount();

    }

    static void invokeBakeryLock(int n, int limit, int version) throws Exception {

        Lock bl = null;
        if (version < 1 || version > 2)
            return;

        if (version == 1) {
            bl = new BakeryLock(n);
            c = new Counter(bl);
        }

        else if (version == 2) {
            bl = new BakeryLockV2(n);
            c = new Counter(bl);
        }

        Thread[] threads = new Thread[n];
        // spawn n threads
        for (int i = 0; i < n; i++) {
            // each thread invokes incrementcounter "limit" times
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < limit; j++) {
                        c.increment();
                    }
                }
            });

            // instantiate lock object with offset to correctly index threads from 0 to n-1
            if (i == 0) {
                bl.setOffset((int) threads[i].getId());
            }

        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            threads[i].start();
        }

        // Wait for each thread to complete using the join() method
        for (Thread thread : threads) {
            try {
                thread.join(); // Main thread waits for each thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long stopTime = System.currentTimeMillis();
        String outputData = String.valueOf((stopTime - startTime));
        outputWriter.write(outputData);
        outputWriter.flush();
        outputWriter.newLine();

        // display count
        c.displayCount();
    }

    static void invokeFilterLock(int n, int limit, int version) throws Exception {

        Lock fl = null;
        if (version < 1 || version > 2)
            return;

        if (version == 1) {
            fl = new FilterLock(n);
            c = new Counter(fl);

        } else if (version == 2) {
            fl = new FilterLockV2(n);
            c = new Counter(fl);
        }

        Thread[] threads = new Thread[n];
        // spawn n threads
        for (int i = 0; i < n; i++) {

            // each thread invokes incrementcounter "limit" times
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < limit; j++) {
                        c.increment();
                    }
                }
            });

            // instantiate lock object with offset to correctly index threads from 0 to n-1
            if (i == 0) {
                fl.setOffset((int) threads[i].getId());
            }

        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            threads[i].start();
        }

        // Wait for each thread to complete using the join() method
        for (Thread thread : threads) {
            try {
                thread.join(); // Main thread waits for each thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long stopTime = System.currentTimeMillis();
        String outputData = String.valueOf((stopTime - startTime));
        outputWriter.write(outputData);
        outputWriter.flush();
        outputWriter.newLine();

        // display count
        c.displayCount();

    }
}