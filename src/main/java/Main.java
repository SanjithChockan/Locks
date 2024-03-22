import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    static Counter c;
    static FileWriter myWriter;
    static BufferedWriter outputWriter;
    // static OutputStreamWriter outputWriter;

    public static void main(String[] args) throws Exception {

        // Driver Code to invoke locks
        int limit = 1000000;
        String lockType = args[0];
        int num = Integer.parseInt(args[1]);
        boolean toAppend = "true".equals(args[2]);

        String filePath = "/home/sxc180101/Desktop/Projects/Locks/src/resources/data/";
        
        try {
            myWriter = new FileWriter(filePath + lockType + "/" + lockType + "-" + num + ".txt", toAppend);
            outputWriter = new BufferedWriter(myWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lockType.equals("Tournament")) {
            invokeLock(new TournamentTree(num), num, limit);
        } else if (lockType.equals("FilterV1")) {
            invokeLock(new FilterLock(num), num, limit);
        } else if (lockType.equals("FilterV2")) {
            invokeLock(new FilterLockV2(num), num, limit);
        } else if (lockType.equals("BakeryLockV1")) {
            invokeLock(new BakeryLock(num), num, limit);
        } else if (lockType.equals("BakeryLockV2")) {
            invokeLock(new BakeryLockV2(num), num, limit);
        } else if (lockType.equals("TASLock")) {
            invokeLock(new TASLock(), num, limit);
        } else if (lockType.equals("TTASLock")) {
            invokeLock(new TTASLock(), num, limit);
        }
        outputWriter.close();
    }

    static void invokeLock(Lock lock, int n, int limit) throws Exception {
        Thread[] threads = new Thread[n];
        c = new Counter(lock);
        int numItersPerThread = limit/n;
        for (int i = 0; i < n; i++) {
            // each thread invokes incrementcounter "limit" times
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < numItersPerThread; j++) {
                        c.increment();
                    }
                }
            });

            // instantiate lock object with offset to correctly index threads from 0 to n-1
            if (i == 0) {
                lock.setOffset((int) threads[i].getId());
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
        System.out.println(outputData + "ms");
         
        outputWriter.write(outputData);
        outputWriter.flush();
        outputWriter.newLine();
        
        // display count
        c.displayCount();
    }
}