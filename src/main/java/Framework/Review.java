package Framework;

import java.util.Arrays;

/**
 * A class that stores the JSON in Review
 */
public class Review {
    private String reviewerID;
    private String asin;
    private String reviewerName;
    private int[] helpful;
    private String reviewText;
    private double overall;
    private String summary;
    private long unixReviewTime;
    private String reviewTime;

    /**
     * The constructor for storing the review JSON
     * @param reviewerID
     * @param asin
     * @param reviewerName
     * @param helpful
     * @param reviewText
     * @param overall
     * @param summary
     * @param unixReviewTime
     * @param reviewTime
     */
    public Review(String reviewerID, String asin, String reviewerName, int[] helpful, String reviewText, double overall, String summary, long unixReviewTime, String reviewTime) {
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

    /**
     * Get the unix time in review
     * @return
     */
    public long getUnixReviewTime() {
        return unixReviewTime;
    }

    @Override
    public String toString() {
        return  '{' +
                "reviewerID='" + reviewerID + '\'' +
                ", asin='" + asin + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                ", helpful=" + Arrays.toString(helpful) +
                ", reviewText='" + reviewText + '\'' +
                ", overall=" + overall +
                ", summary='" + summary + '\'' +
                ", unixReviewTime=" + unixReviewTime +
                ", reviewTime='" + reviewTime + '\'' +
                '}' + '\n';
    }
}
