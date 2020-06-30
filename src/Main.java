import java.util.Random;
import java.util.TreeSet;

public class Main {
    private static double testSet( Set<Integer>q, int opCount){
        return testSet(q,opCount,true);
    }
    private static double testSet( Set<Integer>q, int opCount, boolean debug){
        long startTime =System.nanoTime();
        Random random = new Random();
        int val =0;
        if(debug) {
            for (int i = 0; i < 10; i++) {
                q.add(i);
                System.out.println("Set :" + q);
            }
            q.remove(11);
            System.out.println("Set :" + q);

            for (int i = 0; i < 10; i++) {
                q.remove(i);
                System.out.println("Set :" + q);
            }
            q.remove(11);
        }
        if (debug) System.out.println("test start ->" + startTime/1000000000.0);
        for (int i = 0; i<opCount; i++ ) {
//            val = random.nextInt(opCount);
            val = i;
            q.add(val);
            q.contains(val);

            val = random.nextInt(opCount);
            q.contains(val);
            q.remove(val);
//            if(debug && i%(opCount/100)==0)System.out.print(".");
        }
        long endTime =System.nanoTime();
        q.reset();
        if(debug) System.out.println("\ntest ended ->" +endTime/1000000000.0);

        return (endTime - startTime) /1000000000.0;
    }
    public static void main(String[] args) {
        // write your code here
        AVLset<Integer> avlSet = new AVLset();
        RBTset<Integer> rbtSet = new RBTset<>();
        JavaSet<Integer> javaSet = new JavaSet<>();

        int opCount = 10000;
        double time1 = 0;


        for (int i = opCount;i<1000*opCount; i+= 10*opCount){
            time1 =  testSet(avlSet,i,false);
            System.out.print("isAVL: "+avlSet.isAVL()+"\t");
            System.out.print(i+",");
            System.out.print(time1+"\n");
            time1 =  testSet(rbtSet,i,false);
            System.out.print("isBST: "+rbtSet.isBst()+"\t");
            System.out.print(i+",");
            System.out.print(time1+"\n");

            time1 =  testSet(javaSet,i,false);
            System.out.print("Java:  \t");
            System.out.print(i+",");
            System.out.print(time1+"\n");

        }

    }
}
