package dreamteam.com.homerepair;

/**
 * Represents a Booking object in Firebase
 */
public class Booking {

    private String serviceProviderID, homeOwnerID, service;
    private int startTime;
    private int endTime;

    /**
     * Creates a Booking object
     *
     * @param homeOwnerID String containing the home owner databse id
     * @param serviceProviderID String containing the service provider database id
     * @param service Service object containing the booked service
     * @param startTime int containing the booking start time
     * @param endTime int containing the booking end time
     */
    public Booking (String homeOwnerID, String serviceProviderID, String service, int startTime, int endTime) {

        this.homeOwnerID=homeOwnerID;
        this.serviceProviderID=serviceProviderID;
        this.service=service;
        this.startTime=startTime;
        this.endTime=endTime;
    }

    /**
     * Creates a Booking object
     */
    public Booking () {

    }

    /**
     * Getter method for the home owner id that created the booking
     *
     * @return a String containing the home owner database id
     */
    public String getHomeOwnerID() {

        return homeOwnerID;
    }

    /**
     * Getter method for the service provider database id
     *
     * @return String containing the service provider database id
     */
    public String getServiceProviderID() {

        return serviceProviderID;
    }

    /**
     * Getter method for the booked service
     *
     * @return String containing the booked service
     */
    public String getService() {

        return service;
    }

    /**
     * Getter method for the booking start time
     *
     * @return int containing the booking start time
     */
    public int getStartTime() {

        return startTime;
    }

    /**
     * Getter nethod for the booking end time
     *
     * @return int containing the booking end time
     */
    public int getEndTime() {

        return endTime;
    }
}
