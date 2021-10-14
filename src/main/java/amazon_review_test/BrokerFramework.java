package amazon_review_test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import framework.*;

import java.io.BufferedReader;
import java.io.IOException;


public class BrokerFramework {

    public static JsonConfig config;

    public static void main(String[] args) {

        String inputFile = args[1];
        String line;
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(inputFile))){
            Gson gson = new Gson();
            while ((line = reader.readLine()) != null)
            config = gson.fromJson(line, JsonConfig.class);
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }

        BrokerFactory brokerFactory = new BrokerFactory();
        Broker<Review> broker = brokerFactory.getBroker(config.getBrokerName());

        long start = System.currentTimeMillis(); //retrieve current time when starting calculations

        if (broker == null) {
            System.exit(1);
        }

        instantiatePublishers(broker);

        long end = System.currentTimeMillis(); //retrieve current time when finishing calculations
        System.out.println("time: " + (end-start));

    }

    /**
     * A method that instantiate the publishers and start the program
     * @param broker
     */
    private static void instantiatePublishers(Broker<Review> broker) {
        ReviewPublisher firstFile = new ReviewPublisher(config.getFileName1(), broker);
        Thread p1 = new Thread(firstFile);

        ReviewPublisher secondFile = new ReviewPublisher(config.getFileName2(), broker);
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


}
