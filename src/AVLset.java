public class AVLset<E extends Comparable <E>> implements Set<E> {
    private AVLtree avl;
    public AVLset(){
        avl = new AVLtree(AVLtree.traMode.FMID,false);
    }
    @Override
    public void reset(){
        avl = new AVLtree(AVLtree.traMode.FMID,false);
    }

    @Override
    public int getSize(){
//        return avl.sizeNC(); //size no count: count repeated elements 1 time
        return avl.getSize();
    }
    @Override
    public boolean contains(E e){
        return avl.contains(e);
    }
    @Override
    public boolean isEmpty(){
        return avl.isEmpty();
    }
    @Override
    public void add(E e){
            avl.add(e,null);
    }
    @Override
    public void remove(E e) {
        avl.removeN(e);
    }
    @Override
    public String toString(){
        return avl.toString();
    }
    public boolean isAVL(){
        return avl.isAVL();
    }
}
