package Framework;

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

    public AsyncOrderedDispatchBroker(long milliSeconds, int queueSize) {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.blockingQueue = new BlockingQueue<>(queueSize);
        this.milliSeconds = milliSeconds;
        this.running = true;
        this.poolWorkerThread = new Thread(new PoolWorker());
        poolWorkerThread.start();
    }

    @Override
    public synchronized void publish(T item) {
        // use the BlockingQueue to queue new items as they are being published
        blockingQueue.put(item);
    }

    private class PoolWorker extends Thread {
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

    @Override
    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void shutdown() {
        running = false;
    }
}
