package Framework;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Publisher implements Runnable{
    private static String fileName;
    private String reviewerID;
    private String asin;
    private String reviewerName;
    private int[] helpful;
    private String reviewText;
    private double overall;
    private String summary;
    private long unixReviewTime;
    private String reviewTime;

    public Publisher(String reviewerID, String asin, String reviewerName, int[] helpful, String reviewText, double overall, String summary, long unixReviewTime, String reviewTime) {
        this.reviewerID = reviewerID;
        this.asin = asin;
        this.reviewerName = reviewerName;
        this.helpful = helpful;
        this.reviewText = reviewText;
        this.overall = overall;
        this.summary = summary;
        this.unixReviewTime = unixReviewTime;
        this.reviewTime = reviewTime;
    }

    public static void readFile(String fileName) {
        fileName = fileName;
    }

    public long getUnixReviewTime() {
        return unixReviewTime;
    }

    /**
     * A method that reads in a Json file, and publish the items in synchronous order.
     */
    private static void publishSyncOrder() {
//        Map<Long, Publisher> publisherMap = new HashMap<>();
        SynchronousOrderedDispatchBroker syncOrderBroker = new SynchronousOrderedDispatchBroker();
        String line;
        try (BufferedReader reviewReader = new BufferedReader(new java.io.FileReader(fileName))) {
            Gson gson = new Gson();
            while ((line = reviewReader.readLine()) != null) {
                try {
                    Publisher item  = gson.fromJson(line, Publisher.class);
//                    publisherMap.put(item.getUnixReviewTime(), item);
//                    // publish each file to the subscribers via broker
                    syncOrderBroker.publish(item);
                } catch(com.google.gson.JsonSyntaxException ignored) {}
            }
        } catch (IOException e) {
            System.out.println("Error reading file, please try again");
            System.exit(0);
        }
    }

    /**
     * A method that reads in a Json file, and publish the items in asynchronous order.
     */
    private static void publishAsyncOrder() {
//        Map<Long, Publisher> publisherMap = new HashMap<>();
        AsyncOrderedDispatchBroker asyncOrderBroker = new AsyncOrderedDispatchBroker();
        String line;
        try (BufferedReader reviewReader = new BufferedReader(new java.io.FileReader(fileName))) {
            Gson gson = new Gson();
            while ((line = reviewReader.readLine()) != null) {
                try {
                    Publisher item  = gson.fromJson(line, Publisher.class);
//                    publisherMap.put(item.getUnixReviewTime(), item);
//                    // publish each file to the subscribers via broker
                    asyncOrderBroker.publish(item);
                } catch(com.google.gson.JsonSyntaxException ignored) {}
            }
        } catch (IOException e) {
            System.out.println("Error reading file, please try again");
            System.exit(0);
        }
    }

    /**
     * A method that reads in a Json file, and publish the items in asynchronous unorder.
     */
    private static void publishAsynUnorder() {
//        Map<Long, Publisher> publisherMap = new HashMap<>();
        AsyncUnorderedDispatchBroker asyncUnorderBroker = new AsyncUnorderedDispatchBroker();
        String line;
        try (BufferedReader reviewReader = new BufferedReader(new java.io.FileReader(fileName))) {
            Gson gson = new Gson();
            while ((line = reviewReader.readLine()) != null) {
                try {
                    Publisher item  = gson.fromJson(line, Publisher.class);
//                    publisherMap.put(item.getUnixReviewTime(), item);
//                    // publish each file to the subscribers via broker
                    asyncUnorderBroker.publish(item);
                } catch(com.google.gson.JsonSyntaxException ignored) {}
            }
        } catch (IOException e) {
            System.out.println("Error reading file, please try again");
            System.exit(0);
        }
    }

    @Override
    public void run() {

    }

    // multi instances

    //read in the JSON dataset, parse to each individual objects
    // publish each item
}
