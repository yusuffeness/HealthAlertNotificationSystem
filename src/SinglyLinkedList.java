public class SinglyLinkedList<E> {
    public static class Node<E>{
        private E element;
        private Node<E> next;

        public Node(E element, Node<E> next){
            this.element=element;
            this.next=next;
        }

        public Node<E> getNext(){ return next;}
        public E getElement(){return element;}
        public void setNext(Node<E> n){
            next=n;
        }
    }

    Node<E> head=null;
    Node<E> tail=null;
    private int size=0;

    public SinglyLinkedList(){}

    public int size(){return size;}
    public boolean isEmpty(){return size==0;}

    public E first(){
        if(this.isEmpty()) return null;
         return head.getElement();
    }

    public E last(){
        if(this.isEmpty()) return null;
        return tail.getElement();
    }

    public void addFirst(E e){
       head = new Node<>(e, head); 
         if (size == 0) {         
             tail = head;         
        }
        size++;
    }

    public void addLast(E e){
        Node<E> n= new Node<E>(e, null);
        
       
         if (size == 0) {         
             head=n;        
        }
        else{
            tail.setNext(n);
        }
        tail=n;
        size++;
    }
    public E removeFirst(){
        if(isEmpty()) return null;
        else{
            E answer = head.getElement();
            head=head.getNext();
            size--;
            if(size==0){
                tail=null;
            }
            return answer;
        }
    }
    public E removeLast(){
        if(isEmpty()) return null;
        else{
            Node<E> current=head;
            E answer = tail.getElement();
            if(size==1){
                head=null;
                tail=null;
                
            }
            else{
                while(current.getNext()!=tail){
                    current=current.getNext();
                 }
            
                current.setNext(null);
                tail=current;
               
            }
            size--;
                return answer;
          
        }
    }
    

@Override
public String toString() {
    
    StringBuilder sb = new StringBuilder("(");

    Node<E> walk = head;

    while (walk != null) {
      
        sb.append(walk.getElement());

        walk = walk.getNext();

        if (walk != null) { 
        
            sb.append(", ");
        }
    }
    sb.append(")");
    return sb.toString();
}

@Override
public boolean equals(Object o){

    if(o==this){
        return true;
    }

    if(o==null || (o.getClass()!=this.getClass())){
        return false;
    }

    SinglyLinkedList<E> other = (SinglyLinkedList<E>) o;

    if(size() != other.size()){
        return false;
    }
    Node<E> walk1 = other.head;
    Node<E> walk2 = this.head;

    while(walk2 != null){
        E element1 = walk1.getElement();
        E element2 = walk2.getElement();

        if (! (element1 == null ? element2 == null : element1.equals(element2)) ) {
          
            return false;
        }

        walk1 = walk1.getNext();
        walk2=walk2.getNext();


    }

    return true;







}
}
