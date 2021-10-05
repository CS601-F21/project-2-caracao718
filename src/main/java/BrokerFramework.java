import Framework.Publisher;

import java.util.Map;

/*
Questions:
1. the Publisher class, readFile? etc
2. Publisher run(), includes all three methods? or how?
3. how to get the unixTime in subscribers
4. onEvent is not runnable, so in brokers, how can it be executed?
 */

public class BrokerFramework {

    public static void main(String[] args) {
        // two threads running the Publisher class, one for each file
        Publisher.readFile("reviews_Apps_for_Android_5.json");
        Thread p1 = new Thread() {

        }
    }
}
