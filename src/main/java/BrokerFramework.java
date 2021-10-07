import Framework.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class BrokerFramework {

    public static void main(String[] args) {
        Broker<Review> broker = null;
        String first_file_name = args[1];
        String second_file_name = args[3];

        SynchronousOrderedDispatchBroker<Review> syncOrderBroker = new SynchronousOrderedDispatchBroker<>();
        AsyncOrderedDispatchBroker<Review> asyncOrderBroker = new AsyncOrderedDispatchBroker<>(30000, 100);
        AsyncUnorderedDispatchBroker<Review> asyncUnorderBroker = new AsyncUnorderedDispatchBroker<>(30, 100, 30000);

        try (BufferedReader readCL = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Please input command as the following: \n" +
                    "SynchronousOrderedDispatchBroker\n" +
                    "AsyncOrderedDispatchBroker\n" +
                    "AsyncUnorderedDispatchBroker");
            String input = readCL.readLine();
            if (input == "exit") {
                System.exit(0);
            }
            System.out.println("Please wait...");
            switch (input) {
                case "SynchronousOrderedDispatchBroker" -> broker = syncOrderBroker;
                case "AsyncOrderedDispatchBroker" -> broker = asyncOrderBroker;
                case "AsyncUnorderedDispatchBroker" -> broker = asyncUnorderBroker;
                default -> System.out.println("something wrong with input, please try again");
            }

        } catch (IOException e) {
            System.out.println("Something Wrong");
        }



        long start = System.currentTimeMillis(); //retrieve current time when starting calculations

        if (broker == null) {
            System.exit(1);
        }

        Publisher firstFile = new Publisher(first_file_name, broker);
        Thread p1 = new Thread(firstFile);

        Publisher secondFile = new Publisher(second_file_name, broker);
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

        newSub.closePrintWriter();
        oldSub.closePrintWriter();
        syncOrderBroker.shutdown();
        asyncOrderBroker.shutdown();
        asyncUnorderBroker.shutdown();


        long end = System.currentTimeMillis(); //retrieve current time when finishing calculations
        System.out.println("time: " + (end-start));

    }

}
