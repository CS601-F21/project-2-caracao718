package amazon_review_test;

public class JsonConfig {
    private final String fileName1;
    private final String fileName2;
    private final String output1;
    private final String output2;
    private final String brokerName;

    public JsonConfig(String fileName1, String fileName2, String output1, String output2, String brokerName) {
        this.fileName1 = fileName1;
        this.fileName2 = fileName2;
        this.output1 = output1;
        this.output2 = output2;
        this.brokerName = brokerName;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public String getFileName1() {
        return fileName1;
    }

    public String getFileName2() {
        return fileName2;
    }

    public String getOutput1() {
        return output1;
    }

    public String getOutput2() {
        return output2;
    }
}
