package amazon_review_test;

/**
 * A class that configure all the inputs needed for the program from the command line
 */
public class JsonConfig {
    private String fileName1;
    private String fileName2;
    private String outPut1;
    private String outPut2;
    private String brokerName;

    /**
     * The constructor to instantiate the JsonConfiguration
     * @param fileName1
     * @param fileName2
     * @param output1
     * @param output2
     * @param brokerName
     */
    public JsonConfig(String fileName1, String fileName2, String output1, String output2, String brokerName) {
        this.fileName1 = fileName1;
        this.fileName2 = fileName2;
        this.outPut1 = output1;
        this.outPut2 = output2;
        this.brokerName = brokerName;
    }

    /**
     * A getter to retrieve the brokerName
     * @return
     */
    public String getBrokerName() {
        return brokerName;
    }

    /**
     * A getter to retrieve the first filename
     * @return
     */
    public String getFileName1() {
        return fileName1;
    }

    /**
     * A getter to retrieve the second filename
     * @return
     */
    public String getFileName2() {
        return fileName2;
    }

    /**
     * A getter to retrieve the first output filename
     * @return
     */
    public String getOutPut1() {
        return outPut1;
    }

    /**
     * A getter to retrieve the second output filename
     * @return
     */
    public String getOutPut2() {
        return outPut2;
    }
}
