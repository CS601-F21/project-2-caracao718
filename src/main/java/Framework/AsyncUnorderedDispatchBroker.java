package Framework;

import java.util.concurrent.CopyOnWriteArrayList;

public class AsyncUnorderedDispatchBroker<T> implements Broker<T> {
    private CopyOnWriteArrayList<Subscriber<T>> subscribers = new CopyOnWriteArrayList<>();


    @Override
    public void publish(T item) {

    }

    @Override
    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void shutdown() {

    }
}
