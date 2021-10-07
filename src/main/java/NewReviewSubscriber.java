import Framework.Review;
import Framework.Subscriber;

import java.io.*;

public class NewReviewSubscriber<T> implements Subscriber<T> {
    private long unixTime;

    /**
     * PrintWriter reference: https://www.javatpoint.com/java-printwriter-class
     */
    private PrintWriter writer = null; // closing the writer??
    {
        try {
            writer = new PrintWriter(new File("newOutput.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onEvent(T item) {
        // get the unixTime
        unixTime = ((Review)item).getUnixReviewTime();
        // if the unixTime is more than 1362268800
        if (unixTime >= 1362268800) {
            // write the line in the output file
            writer.print(item);
            writer.flush(); //should I do this? when to close?
        }
    }

}
