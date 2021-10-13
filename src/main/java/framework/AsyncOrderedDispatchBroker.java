package framework;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class that asynchronously publish items to each subscriber in the order which the items were published.
 * @param <T>
 */
public class AsyncOrderedDispatchBroker<T> implements Broker<T> {

    private final CopyOnWriteArrayList<Subscriber<T>> subscribers;
    private final BlockingQueue<T> blockingQueue;
    private final long milliSeconds;
    private volatile boolean running;
    private final Thread poolWorkerThread;

    /**
     * The constructor of the AsyncOrderedDispatchBroker class
     * @param milliSeconds
     * @param queueSize
     */
    public AsyncOrderedDispatchBroker(long milliSeconds, int queueSize) {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.blockingQueue = new BlockingQueue<>(queueSize);
        this.milliSeconds = milliSeconds;
        this.running = true;
        this.poolWorkerThread = new Thread(new PoolWorker());
        poolWorkerThread.start();
    }

    /**
     * A method that publishes each item to the broker, then return immediately
     * @param item
     */
    @Override
    public void publish(T item) {
        // use the BlockingQueue to queue new items as they are being published
        blockingQueue.put(item);
    }

    /**
     * A class that extends Thread, which is runnable, and can be executed by a thread. This class delivers items to each subscriber
     */
    private class PoolWorker extends Thread {
        T currentItem;
        public void run() {
            while (running || !blockingQueue.isEmpty()) {
                currentItem = blockingQueue.poll(milliSeconds);
                if (currentItem != null) {
                    for (Subscriber<T> subscriber : subscribers) {
                        subscriber.onEvent(currentItem);
                    }
                }
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
     * A method that shuts down the broker
     */
    @Override
    public void shutdown() {
        running = false;
        try {
            poolWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
