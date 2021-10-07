import Framework.*;

public class BrokerFramework {

    private static final SynchronousOrderedDispatchBroker<Review> syncOrderBroker = new SynchronousOrderedDispatchBroker<>();
    private static final AsyncOrderedDispatchBroker<Review> asyncOrderBroker = new AsyncOrderedDispatchBroker<>(30000, 100);
    private static final AsyncUnorderedDispatchBroker<Review> asyncUnorderBroker = new AsyncUnorderedDispatchBroker<>(30, 100, 30000);

    public static void main(String[] args) {

        // two threads running the Publisher class, one for each file
        String input;
        Broker<Review> broker;

        long start = System.currentTimeMillis(); //retrieve current time when starting calculations

        Publisher apps = new Publisher("reviews_Apps_for_Android_5.json", syncOrderBroker);
        Thread p1 = new Thread(apps);

        Publisher home = new Publisher("reviews_Home_and_Kitchen_5.json", syncOrderBroker);
        Thread p2 = new Thread(home);

        AmazonSubscriber<Review> newSub = new NewReviewSubscriber<Review>();
        AmazonSubscriber<Review> oldSub = new OldReviewSubscriber<Review>();

        syncOrderBroker.subscribe(newSub);
        syncOrderBroker.subscribe(oldSub);

        p1.start();
        p2.start();


        try {
            p1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            p2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        syncOrderBroker.shutdown();
        asyncOrderBroker.shutdown();
        asyncUnorderBroker.shutdown();
        newSub.closePrintWriter();
        oldSub.closePrintWriter();

        long end = System.currentTimeMillis(); //retrieve current time when finishing calculations
        System.out.println("time: " + (end-start));

    }
}
