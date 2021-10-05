import Framework.Subscriber;

public class OldReviewSubscriber implements Subscriber {
    private long unixTime;

    // reviews before unix time 1362268800
    @Override
    public void onEvent(Object item) {
        // get the unixTime

        // if the unixTime is less than 1362268800
        // then get the item
    }
}
