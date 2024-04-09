import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import spinlocks.Lock;
import spinlocks.ClassicLocks.BakeryLock;
import spinlocks.ClassicLocks.BakeryLockV2;
import spinlocks.ClassicLocks.FilterLock;
import spinlocks.ClassicLocks.FilterLockV2;
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
        boolean testing = true;
        
        if (testing) {

            // counter should be incremented to (limit * num) value
            int num = 20;
            int limit = 200000;
            System.out.println("Testing mode enabled...\n");
            System.out.println("Expectec count value: " + (num * limit) + "\n");


            System.out.println("---Tournament Tree Lock ---");
            testCorrectness(new TournamentTree(num), num, limit);

            System.out.println("---Filter Lock V1---");
            testCorrectness(new FilterLock(num), num, limit);

            System.out.println("---Filter Lock V2---");
            testCorrectness(new FilterLockV2(num), num, limit);

            System.out.println("---Bakery Lock V1---");
            testCorrectness(new BakeryLock(num), num, limit);

            System.out.println("---Bakery Lock V2---");
            testCorrectness(new BakeryLockV2(num), num, limit);


            System.out.println("Shutting down...");
            System.exit(0);
        }

        // Driver Code to invoke locks
        int limit = 250000;
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