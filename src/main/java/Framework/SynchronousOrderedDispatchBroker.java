package Framework;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class that synchronously deliver published items to all subscribers. The publish method will not return to the publisher until all subscribers have completed the onEvent method.
 * Items from different publishers may not interleave.
 */

public class SynchronousOrderedDispatchBroker<T> implements Broker<T> {

    private CopyOnWriteArrayList<Subscriber<T>> subscribers = new CopyOnWriteArrayList<>();
    private volatile boolean canAccept = true;

    @Override
    public synchronized void publish(T item) {
        if (canAccept) {
            for (Subscriber<T> subscriber : subscribers) {
                subscriber.onEvent(item);
            }
        }
    }

    @Override
    public synchronized void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void shutdown() {
        // stop accepting new work
        canAccept = false;
    }
}
