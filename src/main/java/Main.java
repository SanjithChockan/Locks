import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import spinlocks.Lock;
import spinlocks.ClassicLocks.BakeryLock;
import spinlocks.ClassicLocks.BakeryLockV2;
import spinlocks.ClassicLocks.FilterLock;
import spinlocks.ClassicLocks.FilterLockV2;
import spinlocks.QueueLocks.CLHLock;
import spinlocks.QueueLocks.Hemlock;
import spinlocks.QueueLocks.MCSLock;
import spinlocks.TestAndSetLocks.TASLock;
import spinlocks.TestAndSetLocks.TTASLock;
import spinlocks.TreeLocks.TournamentTree;

public class Main {
    static Counter c;
    static FileWriter myWriter;
    static BufferedWriter outputWriter;
    // static OutputStreamWriter outputWriter;

    public static void main(String[] args) throws Exception {
        
        // Test correctness
        boolean testing = false;
        
        if (testing) {
            int num = 20;
            int limit = 100000;
            //testCorrectness(new CLHLock(), num, limit);
            testCorrectness(new Hemlock(), num, limit);
            System.out.println("Is correct: " + (num*limit == c.count));
            System.exit(0);
        }

        // Driver Code to invoke locks
        
        String lockType = args[0];
        int num = Integer.parseInt(args[1]);
        boolean toAppend = "true".equals(args[2]);
        int limit = Integer.parseInt(args[3]);

        String filePath = "/home/sxc180101/Desktop/Projects/Locks/src/resources/data/";
        //String filePath = "/Users/sanjith/Computer Science/Research/multicore/Locks/src/resources/data/";

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
        } else if (lockType.equals("CLHLock")) {
            invokeLock(new CLHLock(), num, limit);
        } else if (lockType.equals("MCSLock")) {
            invokeLock(new MCSLock(), num, limit);
        } else if (lockType.equals("HemLock")) {
            invokeLock(new MCSLock(), num, limit);
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
        
    }

    /*
     * Testing correctness of protocols by checking 
     * if threads increment counter atomically 
     * using implemented locks 
     */
    static void testCorrectness(Lock lock, int n, int limit) throws Exception{


        Thread[] threads = new Thread[n];
        c = new Counter(lock);

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
        System.out.print("Display count: ");
        c.displayCount();
        System.out.println(outputData + "ms");
        System.out.println();
    }
}