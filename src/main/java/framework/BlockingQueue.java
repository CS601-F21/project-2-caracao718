package framework;

/**
 * A class that implements the BlockingQueue
 * @param <T>
 */
public class BlockingQueue<T> {
    private T[] items;
    private int start;
    private int end;
    private int size;

    /**
     * Queue is bounded in size.
     * @param size
     */
    public BlockingQueue(int size) {
        this.items = (T[]) new Object[size];
        this.start = 0;
        this.end = -1;
        this.size = 0;
    }

    /**
     * A method that polls from the waiting queue. If the queue is empty, then the method waits for milliseconds, then return null, or polls from the queue.
     * @return
     */
    public synchronized T poll(long milliSeconds) {
        if (size == 0) {
            long start = System.currentTimeMillis();
            try {
                this.wait(milliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if ((end - start) < milliSeconds) { // should this be less than or equal to?
                return pollItem();
            } else {
                return null;
            }
        }
        return pollItem();
    }

    /**
     * A method that polls one item out of the queue, without checking if the queue is empty. Only use this method if already checked the size of the queue
     * @return
     */
    private T pollItem() {
        T item = items[start];
        start = (start+1)%items.length;
        size--;
        /*
        If the queue was previously full and a new slot has now opened
        notify any waiters in the put method.
         */
        if(size == items.length-1) {
            this.notifyAll();
        }
        return item;
    }

    /**
     * Queue will block until new item can be inserted.
     * @param item
     */
    public synchronized void put(T item) {
        while(size == items.length) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        int next = (end+1)%items.length;
        items[next] = item;
        end = next;
        size++;
        if(size == 1) {
            this.notifyAll();
        }
    }

    /**
     * A method that takes from the waiting queue, if there's no items in the queue, it will wait until there's another item in the queue
     * @return
     */
    public synchronized T take() {
        while(size == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        T item = items[start];
        start = (start+1)%items.length;
        size--;
        /*
        If the queue was previously full and a new slot has now opened
        notify any waiters in the put method.
         */
        if(size == items.length-1) {
            this.notifyAll();
        }

        return item;
    }

    /**
     * A method that checks if the queue is empty
     * @return
     */
    public synchronized boolean isEmpty() {
        return size == 0;
    }


}
