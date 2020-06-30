public class RBTset<E extends Comparable <E>> implements Set<E> {
    private RBtree rbt;
    public RBTset(){
        rbt = new RBtree(RBtree.traMode.FMID,false);
    }
    @Override
    public void reset(){
        rbt = new RBtree(RBtree.traMode.FMID,false);
    }

    @Override
    public int getSize(){
//        return avl.sizeNC(); //size no count: count repeated elements 1 time
        return rbt.getSize();
    }
    @Override
    public boolean contains(E e){
        return rbt.contains(e);
    }
    @Override
    public boolean isEmpty(){
        return rbt.isEmpty();
    }
    @Override
    public void add(E e){
            rbt.add(e,null);
    }
    @Override
    public void remove(E e) {
        rbt.removeX(e);
    }
    @Override
    public String toString(){
        return rbt.toString();
    }
    public boolean isBst(){
        return rbt.isBst();
    }
}
