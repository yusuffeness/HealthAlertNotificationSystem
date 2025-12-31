public interface Queue<E> {
    int size();
    boolean isEmpty();
    E first();
    void enqueue(E e);  // sıra sonuna eleman ekler
    E dequeue();  // önden bir eleman çikar

}
