public class LinkedQueue<E> implements Queue<E> {
    SinglyLinkedList<E> list = new SinglyLinkedList<>();

    public LinkedQueue(){

    }

    public int size(){return list.size();}
    public boolean isEmpty() { return list.isEmpty();}
    public E first() { 
        if(list.size()==0) return null;
        return list.first();

    }

    public void enqueue(E e){
        list.addLast(e);
    }
    public E dequeue(){
        return list.removeFirst();
    }
    public String toString() { return list.toString(); }
}
