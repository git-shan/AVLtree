import java.util.TreeSet;

public class JavaSet<E extends Comparable <E>> implements Set<E> {
    private TreeSet jvs;
    public JavaSet(){
        jvs = new TreeSet();
    }
    @Override
    public void reset(){
        jvs = new TreeSet();
    }

    @Override
    public int getSize(){
//        return avl.sizeNC(); //size no count: count repeated elements 1 time
        return jvs.size();
    }
    @Override
    public boolean contains(E e){
        return jvs.contains(e);
    }
    @Override
    public boolean isEmpty(){
        return jvs.isEmpty();
    }
    @Override
    public void add(E e){
            jvs.add(e);
    }
    @Override
    public void remove(E e) {
        jvs.remove(e);
    }
    @Override
    public String toString(){
        return jvs.toString();
    }
}
