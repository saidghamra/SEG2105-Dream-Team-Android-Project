package dreamteam.com.homerepair;

/**
 * Represents a Rate object from Firebase
 */
public class Rate {

    private int rate;
    private String serviceProviderId, comment;

    /**
     * Creates a Rate object
     *
     * @param serviceProviderId String id of the service provider in the databse
     * @param rate service rate out of 5
     * @param comment String containing a comment about the service
     */
    public Rate (String serviceProviderId, int rate, String comment) {

        this.comment=comment;
        this.serviceProviderId=serviceProviderId;
        this.rate=rate;
    }

    /**
     * Creates a Rate object
     */
    public Rate () {

    }

    /**
     * Getter method for the service comment
     *
     * @return String containing the service comment
     */
    public String getComment() {

        return comment;
    }

    /**
     * Getter method for the service rate
     *
     * @return int containing the service rate out of 5
     */
    public int getRate() {

        return rate;
    }

    /**
     * Getter method for the service provider database id
     *
     * @return String containing the service provider database id
     */
    public String getServiceProviderId() {

        return serviceProviderId;
    }
}
