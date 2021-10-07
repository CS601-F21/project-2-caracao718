import Framework.*;

/*
 - how to get the time in subscriber
 - check if my syncOrdered implementation is right
 - check the main
 - do I need to specify the type in publisher?

 */
public class BrokerFramework {

    private static SynchronousOrderedDispatchBroker<Review> syncOrderBroker = new SynchronousOrderedDispatchBroker<>();
    private static AsyncOrderedDispatchBroker<Review> asyncOrderBroker;
    private static AsyncUnorderedDispatchBroker<Review> asyncUnorderBroker;

    public static void main(String[] args) {

        // two threads running the Publisher class, one for each file
        String broker;
        Publisher apps = new Publisher("test_apps.json", syncOrderBroker);
        Thread p1 = new Thread(apps);
        Subscriber<Review> newSub = new NewReviewSubscriber<Review>();

        Publisher home = new Publisher("test_home.json", syncOrderBroker);
        Thread p2 = new Thread(home);
        Subscriber<Review> oldSub = new OldReviewSubscriber<Review>();

        syncOrderBroker.subscribe(newSub);
        syncOrderBroker.subscribe(oldSub);

        p1.start();
        p2.start();

        try {
            p1.join();
            p2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
