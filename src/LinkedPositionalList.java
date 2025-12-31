import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedPositionalList<E> implements PositionalList<E> {
    private static class Node<E> implements Position<E> {
        private E element;
        private Node<E> prev;
        private Node<E> next;

        public Node(E e, Node<E> p, Node<E> n){
            element=e;
            prev=p;
            next=n;
        }
        public E getElement() throws IllegalStateException{
            if(next==null){
                throw new IllegalStateException("hata");
            }
            return element;
        }
        public Node<E> getPrev() {
         return prev;
     }

     
     public Node<E> getNext() { 
         return next;
     }

    
     public void setElement(E e) { element = e; }
     public void setPrev(Node<E> p) { prev = p; }
     public void setNext(Node<E> n) { next = n; }
    }


    private Node<E> header;
    private Node<E> tailer;
    private int size=0;

    public LinkedPositionalList() {
    header = new Node<>(null, null, null);     
    tailer = new Node<>(null, header, null);   
    header.setNext(tailer);                    
  }


    
    private Node<E> validate (Position<E> p) throws IllegalArgumentException{
        if(!(p instanceof Node)) throw new IllegalArgumentException("error");

        Node<E> node = (Node<E>) p;
        if(node.getNext()==null) throw new IllegalArgumentException("error");

        return node;
    }

    private Position<E> position(Node<E> node){
        if(node==header || node==tailer){
            return null;

        }
        return node;
    }
        public boolean isEmpty() { return size == 0; }
         public int size() { return size; }

    public Position<E> first(){
        return position(header.getNext());
    }
    public Position<E> last(){
        return position(tailer.getPrev());
    }

    public Position<E> before(Position<E> p) throws IllegalArgumentException{
        Node<E> n = validate(p);
        return position(n.getPrev());
    }
    public Position<E> after(Position<E> p) throws IllegalArgumentException{
        Node<E> n = validate(p);
        return position(n.getNext());
    }

    private Position<E> addBetween(E e, Node<E> pred, Node<E> succ) {
    Node<E> newest = new Node<>(e, pred, succ);  
    pred.setNext(newest);
    succ.setPrev(newest);
    size++;
    return newest;
  }
    public Position<E> addFirst(E e){
       return addBetween(e, header, header.getNext());
    }
    public Position<E> addLast(E e){
        return addBetween(e, tailer.getPrev(), tailer);

    }
    public Position<E> addBefore(Position<E> p,E e) throws IllegalArgumentException{
        Node<E> n = validate(p);
       return addBetween(e, n.getPrev(), n);

    }
    public Position<E> addAfter(Position<E> p,E e) throws IllegalArgumentException{
        Node<E> n = validate(p);
       return addBetween(e, n, n.getNext());

    }
    public E set(Position<E> p , E e) throws IllegalArgumentException{
        Node<E> n = validate(p);
        E answer = n.getElement();
        n.setElement(e);

        return answer;
    }
    public E remove(Position<E> n) throws IllegalArgumentException{
        Node<E> node = validate(n);
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        E answ = node.getElement();

        node.setElement(null);
        node.setNext(null);
        node.setPrev(null);
        size--; 

        return answ;
    }

    private class PositionIterator implements Iterator<Position<E>>{
        private Position<E>cursor=first(); 
        private Position<E>recent=null;

        public PositionIterator(){

        }
        public boolean hasNext(){
            return cursor!=null;
        }
        public Position<E> next() throws NoSuchElementException {
            if(cursor==tailer) throw new NoSuchElementException();

            cursor=after(cursor);

            return before(cursor);
        }
        public void remove() throws IllegalStateException{
            if(recent==null) throw new IllegalStateException();

            LinkedPositionalList.this.remove(recent);
            recent=null;
                }
    }

    private class PositionIterable implements Iterable<Position<E>> {
    public Iterator<Position<E>> iterator() { return new PositionIterator(); }
  } 

  
 
  public Iterable<Position<E>> positions() {
    return new PositionIterable();      
  }

  private class ElementIterator implements Iterator<E> {
    Iterator<Position<E>> posIterator = new PositionIterator();
    public boolean hasNext() { return posIterator.hasNext(); }
    public E next() { return posIterator.next().getElement(); } 
    public void remove() { posIterator.remove(); }
  }

 
  
  public Iterator<E> iterator() { return new ElementIterator(); }

    


}
