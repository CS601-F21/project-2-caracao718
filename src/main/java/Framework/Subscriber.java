package Framework;

public interface Subscriber<T> {
    /**
     * Called by the Framework.Broker when a new item
     * has been published.
     * @param item
     */
    public void onEvent(T item);
}
