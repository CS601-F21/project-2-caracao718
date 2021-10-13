package framework;

public class BrokerFactory {

    public Broker getBroker(String brokerName) {
        if (brokerName.equals("SynchronousOrderedDispatchBroker")) {
            return new SynchronousOrderedDispatchBroker();
        } else if (brokerName.equals("AsyncOrderedDispatchBroker")) {
            return new AsyncOrderedDispatchBroker(3000, 100);
        } else if (brokerName.equals("AsyncUnorderedDispatchBroker")) {
            return new AsyncUnorderedDispatchBroker(30);
        } else {
            System.out.println("Wrong input");
        }
        return null;
    }
}
