package Framework;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncUnorderedDispatchBroker<T> implements Broker<T> {
    private final CopyOnWriteArrayList<Subscriber<T>> subscribers;
    private final BlockingQueue<T> blockingQueue;
    private final long milliSeconds;
    private volatile boolean running;
    private ExecutorService threadPool;

    public AsyncUnorderedDispatchBroker(int threadPoolSize, int queueSize, long milliSeconds) {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.blockingQueue = new BlockingQueue<>(queueSize);
        this.milliSeconds = milliSeconds;
        this.running = true;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        threadPool.execute(new Pool());
    }

    @Override
    public void publish(T item) {
        blockingQueue.put(item);
    }

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

    @Override
    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

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
