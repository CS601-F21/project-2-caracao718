package Framework;

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
    private final BlockingQueue<T> blockingQueue;
    private final long milliSeconds;
    private volatile boolean running;
    private ExecutorService threadPool;

    /**
     * The constructor of the AsyncUnorderedDispatchBroker class.
     * @param threadPoolSize
     * @param queueSize
     * @param milliSeconds
     */
    public AsyncUnorderedDispatchBroker(int threadPoolSize, int queueSize, long milliSeconds) {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.blockingQueue = new BlockingQueue<>(queueSize);
        this.milliSeconds = milliSeconds;
        this.running = true;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        threadPool.execute(new Pool());
    }

    /**
     * A method that publishes each item to the broker, then return immediately
     * @param item
     */
    @Override
    public void publish(T item) {
        blockingQueue.put(item);
    }

    /**
     * A class that extends Thread, which is runnable, and can be executed by a thread. This class delivers items to each subscriber
     */
    private class Pool extends Thread {
        T currentItem;
        public void run() {
            while (running) {
                synchronized (blockingQueue) {
                    currentItem = blockingQueue.poll(milliSeconds);
                    if (currentItem != null) {
                        for (Subscriber<T> subscriber : subscribers) {
                            subscriber.onEvent(currentItem);
                        }
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
     * A method that shuts down the broker and the threadpool
     */
    @Override
    public void shutdown() {
        threadPool.shutdown();
        running = false;
        try {
            threadPool.awaitTermination(300, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
