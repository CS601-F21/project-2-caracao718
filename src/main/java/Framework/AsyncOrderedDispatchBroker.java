package Framework;

import java.util.concurrent.CopyOnWriteArrayList;

public class AsyncOrderedDispatchBroker<T> implements Broker<T> {

    private CopyOnWriteArrayList<Subscriber<T>> subscribers = new CopyOnWriteArrayList<>();

    @Override
    public void publish(T item) {
        // use the BlockingQueue to queue new items as they are being published


    }

    @Override
    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void shutdown() {

    }
}
