package dreamteam.com.homerepair;

/**
 * Represents a service object from Firebase
 */
public class Service {

    private String name;
    private int hourlyRate;

    /**
     * Creates a service object
     * @param name the service name
     * @param hourlyRate the service's hourly rate
     */
    public Service(String name, int hourlyRate){
        this.name = name;
        this.hourlyRate = hourlyRate;
    }

    /**
     * Creates a service object
     */
    public Service () {

    }

    /**
     * Gets the name of the service
     * @return the service's name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the service's hourly rate
     * @return the service's hourly rate
     */
    public int getHourlyRate(){
        return this.hourlyRate;
    }

    /**
     * Prints a nice representation of the
     * service object
     * @return String containing the service
     * name and hourly rate
     */
    public String toString() {

        return name + ": $" + hourlyRate;
    }
}
