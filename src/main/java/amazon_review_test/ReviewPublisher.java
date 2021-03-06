package amazon_review_test;

import com.google.gson.JsonParseException;
import framework.Broker;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A class that reads in a file, parse the file to a JSON file, then publish it.
 */
public class ReviewPublisher implements Runnable{
    private String fileName;
    private Broker<Review> brokerType;

    public ReviewPublisher(String fileName, Broker<Review> broker) {
        this.fileName = fileName;
        this.brokerType = broker;
    }

    /**
     * A method that reads in a Json file, and publish the items in AmazonReviewTest.Review.
     */
    private void publish() {
        String line;
        try (BufferedReader reviewReader = new BufferedReader(new java.io.FileReader(fileName))) {
            Gson gson = new Gson();
            while ((line = reviewReader.readLine()) != null) {
                try {
                    Review item = gson.fromJson(line, Review.class);
                    brokerType.publish(item);
                } catch(JsonParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file, please try again");
            System.exit(0);
        }
    }

    /**
     * Runnable object to run in a thread
     */
    @Override
    public void run() {
        publish();
    }

}
