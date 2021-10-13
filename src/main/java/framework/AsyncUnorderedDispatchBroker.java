package framework;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A class that asynchronously publish items to each subscriber, and the order doesn't matter
 * @param <T>
 */
public class AsyncUnorderedDispatchBroker<T> implements Broker<T> {
    private final CopyOnWriteArrayList<Subscriber<T>> subscribers;
    private volatile boolean running;
    private final ExecutorService threadPool;

    /**
     * The constructor of the AsyncUnorderedDispatchBroker class.
     * @param threadPoolSize
     */
    public AsyncUnorderedDispatchBroker(int threadPoolSize) {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.running = true;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * A method that publishes each item to the broker, then return immediately
     * @param item
     */
    @Override
    public void publish(T item) {
        if (running) {
            threadPool.execute(new PublishItem(item));
        }
    }

    /**
     * A class that implements runnable, and can be executed by a thread. This class delivers items to each subscriber
     */
    private class PublishItem implements Runnable {
        T currentItem;
        public PublishItem(T currentItem) {
            this.currentItem = currentItem;
        }

        @Override
        public void run() {
            for (Subscriber<T> subscriber : subscribers) {
                subscriber.onEvent(currentItem);
            }
        }
    }

    /**
     * A method that allows subscribers to subscribe to the publisher
     * @param subscriber
     */
    @Override
    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * A method that shuts down the broker and the threadpool
     */
    @Override
    public void shutdown() {
        running = false;
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(300, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
