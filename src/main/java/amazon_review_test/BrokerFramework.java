package amazon_review_test;

import framework.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class BrokerFramework {

    public static void main(String[] args) {
        Broker<Review> broker;
        String first_file_name = args[1];
        String second_file_name = args[3];

        BrokerFactory brokerFactory = new BrokerFactory();
        broker = getReviewBroker(null, brokerFactory);

        long start = System.currentTimeMillis(); //retrieve current time when starting calculations

        if (broker == null) {
            System.exit(1);
        }

        instantiatePublishers(broker, first_file_name, second_file_name);

        long end = System.currentTimeMillis(); //retrieve current time when finishing calculations
        System.out.println("time: " + (end-start));

    }

    /**
     * A method that instantiate the publishers and start the program
     * @param broker
     * @param first_file_name
     * @param second_file_name
     */
    private static void instantiatePublishers(Broker<Review> broker, String first_file_name, String second_file_name) {
        ReviewPublisher firstFile = new ReviewPublisher(first_file_name, broker);
        Thread p1 = new Thread(firstFile);

        ReviewPublisher secondFile = new ReviewPublisher(second_file_name, broker);
        Thread p2 = new Thread(secondFile);

        AmazonSubscriber<Review> newSub = new NewReviewSubscriber<Review>();
        AmazonSubscriber<Review> oldSub = new OldReviewSubscriber<Review>();

        broker.subscribe(newSub);
        broker.subscribe(oldSub);

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

        broker.shutdown();
        newSub.closePrintWriter();
        oldSub.closePrintWriter();
    }

    /**
     * A method that reads in the broker name from the command line, then instantiate a new broker
     * @param broker
     * @param brokerFactory
     * @return
     */
    private static Broker<Review> getReviewBroker(Broker<Review> broker, BrokerFactory brokerFactory) {
        try (BufferedReader readCL = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Please input command as the following: \n" +
                    "SynchronousOrderedDispatchBroker\n" +
                    "AsyncOrderedDispatchBroker\n" +
                    "AsyncUnorderedDispatchBroker");
            String input = readCL.readLine();
            if (Objects.equals(input, "exit")) {
                System.exit(0);
            }
            System.out.println("Please wait...");
            broker = brokerFactory.getBroker(input);

        } catch (IOException e) {
            System.out.println("Something Wrong");
        }
        return broker;
    }

}
