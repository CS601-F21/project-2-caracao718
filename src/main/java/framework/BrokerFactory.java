package framework;

/**
 * A class that is used for the Factory Design Pattern
 */
public class BrokerFactory {

    /**
     * A method that takes the string of broker name and return a Broker object
     * @param brokerName
     * @return
     */
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
