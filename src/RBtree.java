import java.util.LinkedList;
import java.util.Queue;

public class RBtree<K extends Comparable<K>, V > {
    private enum COLORS {
        RED ,
        BLK
        }

    private class Node{
        public K k;
        public V v;
        public int size,count;
        public Node left,right;
        public COLORS color;

        public Node(K k, V v){
            this();
            this.k = k;
            this.v = v;
        }
        public Node(){
            this.k = null;
            this.v = null;
            left = null;
            right = null;
            size = 1;  // size of tree, using this node as root
            count = 0; // value = e
            color = COLORS.RED;
        }
        public void copyValue(Node src){
            k = src.k;
            v = src.v;
            count = src.count;
        }
        @Override
        public String toString(){
            StringBuilder res = new StringBuilder();
            res.append("("+ k+":"+v+","+color+";"+count+","+size+")");
            return res.toString();
        }
    }
    private Node root;
    private boolean allowCount;
    private boolean deleting;
    public  static Array <StringBuilder> bstStringArr = new Array<>();
    private static final int maxDepth = 1; //for demo and debug use.
    public enum traMode {
        PPRE, // pre order
        FMID, // forward in the middle: in order
        POST, // post order
        IMID, // inverse in the middle: inverse in order
        LEVL, // leveled order
        TREE  // human readable tree
    }
    private COLORS getColor(Node node){
        if(node==null)
            return COLORS.BLK;
        else
            return node.color;
    }
    public Boolean isBst(){
       return isBst(root);
    }
    private boolean isBst(Node node){
        Array <K> keys = new Array<>();
        inOrder(node,keys);
        for(int i=1;i<keys.getSize();i++){
            if(keys.get(i).compareTo(keys.get(i-1))<=0)
                return false;
        }
        return true;
    }
    private void inOrder(Node node, Array keys){
        if(node==null)
            return;

        inOrder(node.left,keys);
        keys.addLast(node.k);
        inOrder(node.right,keys);
    }

    public void setAllowCount(boolean allowCount) {
        this.allowCount = allowCount;
    }
    public boolean getAllCount(){
        return allowCount;
    }

    private traMode traverse;
    public traMode getTraverse() {
        return traverse;
    }
    public void setTraverse(traMode mode) {
        traverse = mode;
    }

    private void initBstArr(){
        traverse = traMode.TREE;
        for(int i=0; i<maxDepth;i++){
            bstStringArr.addLast(new StringBuilder());
        }
    }

    public RBtree(traMode traverse, boolean allowCount){
        this();
        setAllowCount(allowCount); // override default config
        setTraverse(traverse);
    }
    public RBtree(boolean allowCount) {
        this();
        setAllowCount(allowCount); // override default config
    }
    public RBtree() { // leveled order
        root = null;
        deleting = false;
        allowCount = true;
        initBstArr();
    }


    public RBtree(K[] arrK, V[] arrV, boolean allowCount){
        this(allowCount);
        for (int i=0;i< arrK.length;i++ ) {
            add(arrK[i],arrV[i]);
        }
    }
    public int getSize() {
        return root.size;
    }
    public boolean isEmpty(){
        return (root.size ==0);
    }
    private boolean isEmpty(Node node){
        return (node.size ==0);
    }
    private boolean isRed(Node node){
        return getColor(node) == COLORS.RED;
    }
    private void flipColor(Node node){
        node.color = COLORS.RED;
        node.left.color = COLORS.BLK;
        node.right.color = COLORS.BLK;
    }
//--------------------------------------------
//       y
//     x    t4                x
//   z   t3       ==>      z      y
// t1  t2               t1  t2  t3  t4
//    t1<z<t2<x<t3<y<t4
//--------------------------------------------
    private Node rotateRight(Node y){
        Node x = y.left;
        Node t3 = x.right;
        x.right = y;
        y.left = t3;

//        update color and size;
        x.color = y.color;
        y.color = COLORS.RED;
        y.size = 1  + size(y.left) + size(y.right) + y.count;
        x.size = 1  + size(x.left) + size(x.right) + x.count;
        return x;
    }
//--------------------------------------------
//       y
//    t4    x                    x
//       t3   z     ==>       y      z
//         t2   t1          t4  t3  t2  t1
//    t1>z>t2>x>t3>y>t4
//--------------------------------------------
    private Node rotateLeft(Node y){
        Node x = y.right;
        Node t3 = x.left;
        x.left = y;
        y.right = t3;

//        update color and size;
        x.color = y.color;
        y.color = COLORS.RED;
        y.size = 1  + size(y.left) + size(y.right) + y.count;
        x.size = 1  + size(x.left) + size(x.right) + x.count;
        return x;
    }
    private Node maintainRBT(Node node) {
        if(node == null) return null;
        if (!isRed(node.left) && isRed(node.right))
            node = rotateLeft(node);

        if (isRed(node.left) && isRed(node.left.left))
            node = rotateRight(node);

        if (isRed(node.left) && isRed(node.right))
            flipColor(node);
        return node;
    }
    public void add(K k, V v){
        root = add(root, k, v, 0);
        root.color = COLORS.BLK;
    }
    private Node add(Node node, K k, V v, int depth){
//        basic solution, 3 case of value e to stop traversing; consider null tree as a BST,LBST,RBST
        if(node == null){ //null then create
            Node newNode =  new Node(k,v);
            return newNode;
        }
//        recursively add
        int cmp = k.compareTo(node.k);
        if(cmp<0){  // less, add left
            node.left = add(node.left, k, v, depth+1);
        }
        else if(cmp>0) { // greater, add right
            node.right = add(node.right, k,v, depth+1);
        }else{
            if(allowCount)node.count++;  //equal then add count 1
        }

        if(allowCount){
            node.size++; //any e will be added
        }else
            node.size = 1  + size(node.left) + size(node.right);
//        update height of node
        node = maintainRBT(node);
        return node; //not null, value equal, then return node itself
    }

    private int size(Node node){
        return (node == null) ? 0: node.size;
    }
    public int sizeNC(){
        return size(root)-sumCount(root);
    }
    public int sumCount(){
        if(!allowCount) return 0;
        else return sumCount(root);
    }
    private int sumCount(Node node){
        if(node==null) return 0;
        return node.count + sumCount(node.left) + sumCount(node.right);
    }

    public V remove(K k){
        Node res = find(k);
        if(res!=null) {
            removeN(k);
            return res.v;
        }
        return null;
    }

    public boolean set(K k, V v){
        Node res = find(k);
        if(res!=null)
            res.v = v;
        return (res!=null);
    }

    public V get(K k){
        Node res = find(k);
        return (res==null ? null: res.v);
    }
    public int rank(K k){
        return rank(root, k);
    }

    public int rank(Node node, K k){
        if(node == null)
            return  0;

        int cmp = k.compareTo(node.k);
        if (cmp<0) return rank(node.left, k);
        else if(cmp>0) return 1+ node.count + size(node.left) + rank(node.right, k);
        else return size(node.left);

    }

    public Node select(int rank){
        return select(root,rank);
    }

    public Node select(Node node, int rank){
        if(rank<0 || rank > getSize() || node ==null)
            return null;

        int cmp1 = rank - size(node.left); //compare to rank of left child
        int cmp2 = rank - (size(node.left) + node.count +1); //compare to rank of  right child
         if(cmp1 < 0){
             return select(node.left, rank);
         }else if(cmp2 >=0 ){
             return select(node.right, cmp2);
         }else //between cmp1 and cmp2, [cmp1, cmp2) which is matched.
             return node;
    }

    public Node ceil(K k){ // min of  values in tree which value >= e
        int ceiRank = rank(k);
//        Node res = select(ceiRank);
//        if(res.e.equals(e))
//            ceiRank = ceiRank+1;
        return select(ceiRank);
    }

    public Node floor(K k){ // max of values in tree which value <= e
        int floorRank = rank(k);
        Node res = select(floorRank);
        if(res.k.compareTo(k)>0) floorRank = floorRank -  1;
        return select(floorRank);
    }

    public Node find(K k){
        return find(root, k);
    }
    public Node find(Node node, K k){
        if(node == null)
            return null;

        int cmp = k.compareTo(node.k);
        if(cmp==0) {
            return node;
        }else if(cmp<0){
            return find(node.left, k);
        }else
            return find(node.right, k);
    }

    public boolean contains(K k){
        return contains(root, k);

    }
    private boolean contains(Node node, K k){
        if(node == null)
            return false;
        int cmp = k.compareTo(node.k);
        if(cmp ==0){
            return true;
            }
        else if(cmp <0){
            return contains(node.left, k);
        }else
            return contains(node.right, k);
    }
    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append("TRM-"+traverse+":\t");
        if(traverse == traMode.TREE){
            // clear string builders
            for(int i=0; i<bstStringArr.getSize();i++){
                if(bstStringArr.get(i).length()!=0)
                    bstStringArr.get(i).delete(0,bstStringArr.get(i).length());
            }
//        public void printTree(){
            bstStringArr.get(0).append("BST->");
            iToStringTree(root, 0);
            for(int i=0; i<bstStringArr.getSize(); i++){
                if(bstStringArr.get(i).length()!=0) {
                    bstStringArr.get(i).insert(0,"L"+i+"\t");
//                System.out.println(bstStringArr.get(i).toString());
                    res.append(bstStringArr.get(i).toString()+"\n");
                }
            }
        }
        if(traverse == traMode.PPRE){
            res = iToStringPre(res, root,0);
        }

        if(traverse == traMode.IMID){
            res = iToStringIin(res, root,0);
        }

        if(traverse == traMode.FMID){
          res = iToStringIn(res, root,0);
        }

        if(traverse == traMode.POST){
            res = iToStringPost(res, root,0);
        }

        if(traverse == traMode.LEVL){
            res = iToStringLev(res, root);
        }
        return res.toString();
    }
    private StringBuilder iToStringLev(StringBuilder res, Node node){
        Queue <Node> queue = new LinkedList<>();
        Node cur;
        queue.add(node);
        while(!queue.isEmpty()){
            cur = queue.remove();
            if(cur!=null) {
                for(int i=0;i<=cur.count;i++) res.append(cur.k+":"+cur.v+","); // process node in queue
                queue.add(cur.left);
                queue.add(cur.right);
            }
        }
        return res;
    }
    private StringBuilder iToStringPre(StringBuilder res, Node node, int depth){
        if(node==null) return res;
        for(int i=0;i<=node.count;i++) res.append(node.k+":"+node.v +",");

// pre position
        res=iToStringPre(res,node.left,depth+1);
// in position
        res=iToStringPre(res,node.right,depth+1);
// post position

        return res;
    }
    private StringBuilder iToStringPost(StringBuilder res, Node node, int depth){
        if(node==null) return res;
// pre position
        res=iToStringPost(res,node.left,depth+1);
// in position
        res=iToStringPost(res,node.right,depth+1);
// post position
        for(int i=0;i<=node.count;i++) res.append(node.k+":"+node.v +",");

        return res;
    }

    private StringBuilder iToStringIin(StringBuilder res, Node node, int depth){
        if(node==null) return res;

// pre position
        res=iToStringIin(res,node.right,depth+1);
// in position
        for(int i=0;i<=node.count;i++) res.append(node.k+":"+node.v +",");
        res=iToStringIin(res,node.left,depth+1);
// post position

        return res;
    }

    private StringBuilder iToStringIn(StringBuilder res, Node node, int depth){
        if(node==null) return res;

// pre position
        res=iToStringIn(res,node.left,depth+1);
// in position
        for(int i=0;i<=node.count;i++) res.append(node.k+":"+node.v +",");
        res=iToStringIn(res,node.right,depth+1);
// post position

        return res;
    }

    private void iToStringTree(Node node, int depth){
       for (int i = bstStringArr.getSize();i<=depth+1;i++){
           bstStringArr.addLast(new StringBuilder());
       }
       if(node==null) {
           bstStringArr.get(depth).append("o,");
           return;
       }
       else{
           bstStringArr.get(depth).append(node+ ",");
       }

// pre position
       bstStringArr.get(depth+1).append("{L->");
       iToStringTree(node.left,depth+1);
// in position
       bstStringArr.get(depth+1).append("R->");
       iToStringTree(node.right,depth+1);
       bstStringArr.get(depth+1).deleteCharAt(bstStringArr.get(depth+1).length()-1);// remove "," for right node
       bstStringArr.get(depth+1).append("};\t");
// post position
    }

    public void removeMin(){
        root = removeMin(root);
    }
    public Node min(){
        return min(root);
    }
    private Node min(Node node){
        // A min value node is a left leaf or a left node with right child only
        if(node == null)
            return null;
        else if(node.left == null)
            return node;

        //walk left nodes
        return min(node.left);
    }

    private Node removeMin(Node node){
        //basic left node (a leaf node of left tree, or a left node with right child only)
        if(node == null)
            return null;
        else if(node.left == null) {//use right child to replace this node (min value node)
            if(node.count!=0){ //multiple value in same node
                node.count--;
                return node;
            }
            else{
                return node.right;
            }
        }

        //walk through left branch
        node.left  = removeMin(node.left);
        if(node!=null) node.size--;   // the min value must be removed
        //        update height of node
        node = maintainRBT(node);
        return node;
        }

    public Node max(){
        return max(root);
    }
    private Node max(Node node){
        // A max value node is a right leaf or a right node with left child only
        if(node == null)
            return null;
        else if(node.right == null)
            return node;

        //walk through right branch
        return max(node.right);
    }

    public void removeMax(){
        root = removeMax(root);
    }
    private Node removeMax(Node node){
        //basic right node (a leaf node of right tree, or a right node with left child only)
        if(node == null)
            return null;
        else if(node.right == null){ //use its left child to replace this node(max value node)
            if(node.count!=0) { //multiple value in same node
                node.count--;
                return node;
            }
            else {
                if(isRed(node.left)) node.left.color = COLORS.BLK;
                return node.left;
            }
        }

        //walk through right branch
        node.right  = removeMax(node.right);
        if(node!=null) node.size--;   // the max value must be removed

        //        update height of node
        node = maintainRBT(node);
        return node;
    }
    public void removeN(K k){ // remove with remove miN
        root = removeVn(root, k);
        deleting = false;
    }
    public void removeX(K k){ // remove with remove maX
        root = removeVx(root, k);
        deleting = false;
    }
    private Node removeVx(Node node, K k){ // using max to remove val
        if(node == null)
            return null;

        if(k.compareTo(node.k) >0) { //value is greater, then looking right node
            node.right = removeVx(node.right, k);
        }else if(k.compareTo(node.k) < 0){ //value is smaller, then looking left node
            node.left = removeVx(node.left, k);
        }else { //(e.compareTo(node.e) == 0) found value matched node
            deleting = true;
            if(node.count!=0) { // if multiple value in one node, just decrease count
                node.count--;
            }else if (node.left != null) { //had left children use max of left children to replace this node
                node.copyValue(max(node.left));
                node.left = removeMax(node.left);
            }
            else if(node.right!=null) { //only had right child, use right child to replace this node
                node = node.right;
            }
            else {
                node = null; // no children, delete this node
            }
        }
        if (node != null && deleting) node.size--;
        //        update height of node
        node = maintainRBT(node);
        return node;
    }
    private Node removeVn(Node node, K k){ // using min to remove val
        if(node == null)
            return null;

        if(k.compareTo(node.k) >0) {
            node.right = removeVn(node.right, k);
        }else if(k.compareTo(node.k) < 0){
            node.left = removeVn(node.left, k);
        }else{ //(e.compareTo(node.e) == 0) found value matched node
            deleting = true;
            if(node.count!=0) { // if multiple value in one node, just decrease count
                node.count--;
            }
            else if(node.right!=null) {  // had right child, use min of right child to replace this node
                node.copyValue(min(node.right));
                node.right = removeMin(node.right);
            }
            else if(node.left!=null) {// left child only , use left child to replace this node
                if(isRed(node.left)) node.left.color = COLORS.BLK;
                node = node.left;
            }
            else {
                node = null;  // no children, delete this node
            }
        }
        // update size of node and its parent nodes
        if (node != null && deleting) node.size--;
        //        update height of node
        node = maintainRBT(node);
        return node;
    }
    private String generateDepthString (int depth) {
        StringBuilder depthString = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            depthString.append("|--");
        }
        return depthString.toString();
    }
    public void printDebug() {
        System.out.println("size="+root.size);
        setTraverse(traMode.FMID);
        System.out.println(this);
        setTraverse(traMode.TREE);
        System.out.println(this);
    }
    private void testRemoveMin(int count){
        for(int i=0; i<count; i++){
            System.out.println("Remove min");
            removeMin();
            printDebug();
        }
    }
    private void testRemoveMax(int count){
        for(int i=0; i<count; i++){
            System.out.println("Remove max");
            removeMax();
            printDebug();
        }
    }
    private void testRemoveXN(K k, V v) {
        System.out.println("Remove Value N: " + k);
        this.removeN(k);
        this.printDebug();
        this.add(k,v);  // add back
        System.out.println("add " + k + " back, size=" + root.size);
        System.out.println(this);
        System.out.println("Remove Value X: " + k);
        this.removeX(k);
        this.printDebug();
        System.out.println("Remove Value N: " + k);
        this.removeN(k);
        this.printDebug();
    }

    public static void main(String[] args){
        Integer [] data = {1,2,3,4,5,6,7,8,9,10, 11,12,13,35,34,33,32,31,30,29,28,14,15,16,17,18,27,26,25,24};
        RBtree<Integer, Integer> bst = new RBtree<Integer,Integer>(data,data,true);
        System.out.println("size="+bst.getSize());
        for(traMode mode: traMode.values()) {
            bst.setTraverse(mode);
            System.out.println(bst);
        }

        System.out.println( "sum of count = "+ bst.sumCount());
        System.out.println( "sizeNC = "+ bst.sizeNC());

        System.out.println("find 32 :" + bst.find(32));
        System.out.println("find 25 :" + bst.find(25));
        System.out.println( "0 is ranked as #"+ bst.rank(0));
        System.out.println( "25 is ranked as #"+ bst.rank(25));
        System.out.println( "65 is ranked as #"+ bst.rank(65));

        for (Integer i: data){
            System.out.println(i +" is ranked as #"+ bst.rank(i));
        }

        for (int i=0; i<bst.getSize();i++)
        System.out.println( "Selected #"+i+"="+ bst.select(i));

        bst.testRemoveMax(5);
        bst.testRemoveMin(5);
        System.out.println("min of bst: "+bst.min());
        System.out.println("max of bst: "+bst.max());


        for (Integer i = 8; i<37; i=i+4){
            bst.testRemoveXN(i,i);
        }

        System.out.println(bst.contains(11));
        System.out.println(bst.contains(121));
        System.out.println(bst.ceil(21));
        System.out.println(bst.rank(21));
        System.out.println(bst.floor(21));
//        bst.setRankAsCeil(false);
        System.out.println(bst.ceil(4));
        System.out.println(bst.rank(4));
        System.out.println(bst.floor(4));

        for(int i=77; i<88; i++)
            bst.add(i,i);
        bst.set(18,666);
        bst.set(17,777);
        System.out.println(bst);
        System.out.println( "sum of count = "+ bst.sumCount());
        System.out.println( "sizeNC = "+ bst.sizeNC());
        System.out.println( "isBST: "+ bst.isBst());
    }

}
