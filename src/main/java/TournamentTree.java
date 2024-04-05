import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// peterson's algorithm for 2 process generenalized to n processes using a tournament tree
public class TournamentTree extends Lock {

    int n;
    int numOfLeaves;
    int maxNodes;
    private int offset = 0;

    // array to represent flags on each node. Also represented as a tree
    AtomicBoolean[] flags;
    // array to represent victim on each node
    AtomicInteger[] victims;

    // hashmap to backtrack and release locks
    ConcurrentHashMap<Integer, ArrayList<Integer>> stacks = new ConcurrentHashMap<Integer, ArrayList<Integer>>();

    int numOfLevels;

    public TournamentTree(int n) {
        this.n = n;
        numOfLeaves = setNumberOfLeaves(n);
        numOfLevels = setLevel(numOfLeaves);
        maxNodes = (numOfLeaves * 2) - 1;

        flags = new AtomicBoolean[maxNodes];
        victims = new AtomicInteger[numOfLeaves - 1];
        for (int i = 0; i < flags.length; i++) {
            flags[i] = new AtomicBoolean(false);
        }
        for (int i = 0; i < victims.length; i++) {
            victims[i] = new AtomicInteger(-1);
        }
        for (int i = 0; i < n; i++) {
            stacks.put(i, new ArrayList<Integer>());
        }
    }

    @Override
    public void lock() {

        int id = ((int) Thread.currentThread().getId()) - offset;
        int i = id;
        for (int l = 1; l <= numOfLevels; l++) {

            int side = i % 2; // either 0 or 1
            // set flag
            flags[i].set(true);
            // contending thread index
            int j = (side != 0) ? i - 1 : i + 1;
            // set victim
            int victimIndex = (i != 0) ? i / 2 : 0;
            victims[victimIndex].set(i);

            // spin until thread is victim or flag is off for contending thread is true
            while (flags[j].get() && victims[victimIndex].get() == i) {
            }
            ;

            stacks.get(id).add(i);
            // move up in tree
            // i = Math.max(i,j) + ((maxNodes-victimIndex)/2);
            i = ((maxNodes / 2) - (victimIndex)) + (Math.max(i, j));
        }

    }

    @Override
    public void unlock() {
        // set flag to false at every node the thread contended
        int id = ((int) Thread.currentThread().getId()) - offset;
        while (!stacks.get(id).isEmpty()) {
            flags[stacks.get(id).remove(stacks.get(id).size() - 1)].set(false);
        }
    }

    private int setLevel(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    private int setNumberOfLeaves(int n) {
        int x = 1;
        while (x < n) {
            x *= 2;
        }
        return x;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
