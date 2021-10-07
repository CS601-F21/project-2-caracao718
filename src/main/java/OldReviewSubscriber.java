import Framework.Review;
import Framework.Subscriber;

import java.io.*;

/**
 * A class that subscribes only to the old reviews
 * @param <T>
 */
public class OldReviewSubscriber<T> implements AmazonSubscriber<T> {
    private long unixTime;

    /**
     * PrintWriter reference: https://www.javatpoint.com/java-printwriter-class
     */
    private PrintWriter writer2 = null;
    {
        try {
            writer2 = new PrintWriter(new File("oldOutput.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that filters out the older items and print it to the output file
     * @param item
     */
    @Override
    public void onEvent(T item) {
        // get the unixTime
        unixTime = ((Review)item).getUnixReviewTime();
        // if the unixTime is less than 1362268800
        if (unixTime < 1362268800) {
            // write the line in the output file
            writer2.print(item);
            writer2.flush();
        }
    }

    /**
     * A method that closes the writer
     */
    @Override
    public void closePrintWriter() {
        writer2.close();
    }
}
