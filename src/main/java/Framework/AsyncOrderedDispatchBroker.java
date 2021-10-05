package Framework;

import java.util.ArrayList;

public class AsyncOrderedDispatchBroker implements Broker {

    private ArrayList<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void publish(Object item) {
        // use the BlockingQueue to queue new items as they are being published

    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void shutdown() {

    }
}
