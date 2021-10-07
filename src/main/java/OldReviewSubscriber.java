import Framework.Review;
import Framework.Subscriber;

import java.io.*;

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


    @Override
    public void closePrintWriter() {
        writer2.close();
    }
}
