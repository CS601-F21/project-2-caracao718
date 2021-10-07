import Framework.Subscriber;

public interface AmazonSubscriber<T> extends Subscriber<T> {
    /**
     * A method that closes the PrintWriter
     */
    public void closePrintWriter();
}
