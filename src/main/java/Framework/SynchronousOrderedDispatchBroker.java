package Framework;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class that synchronously deliver published items to all subscribers. The publish method will not return to the publisher until all subscribers have completed the onEvent method.
 * Items from different publishers may not interleave.
 */

public class SynchronousOrderedDispatchBroker<T> implements Broker<T> {

    private final CopyOnWriteArrayList<Subscriber<T>> subscribers;
    private volatile boolean canAccept;

    /**
     * The constructor of the SynchronousOrderedDispatchBroker class
     */
    public SynchronousOrderedDispatchBroker() {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.canAccept = true;
    }

    /**
     * A method that publishs all items to all subscribers
     * @param item
     */
    @Override
    public synchronized void publish(T item) {
        if (canAccept) {
            for (Subscriber<T> subscriber : subscribers) {
                subscriber.onEvent(item);
            }
        }
    }

    /**
     * A method that allows subscribers to subscribe to the publisher
     * @param subscriber
     */
    @Override
    public synchronized void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * A method that shuts down the broker
     */
    @Override
    public void shutdown() {
        // stop accepting new work
        canAccept = false;
    }
}
