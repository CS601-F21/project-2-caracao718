package Framework;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A class that synchronously deliver published items to all subscribers. The publish method will not return to the publisher until all subscribers have completed the onEvent method.
 * Items from different publishers may not interleave.
 */

public class SynchronousOrderedDispatchBroker implements Broker { //should implement this as runnable and execute in the main?

    private ArrayList<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void publish(Object item) {

        ExecutorService threadPool = Executors.newFixedThreadPool(50);

        for (Subscriber subscriber : subscribers) {
            threadPool.execute(subscriber.onEvent(item));
        }

    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void shutdown() {
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
