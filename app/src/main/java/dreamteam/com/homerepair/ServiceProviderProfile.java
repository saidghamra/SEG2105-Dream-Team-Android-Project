package dreamteam.com.homerepair;

import java.util.ArrayList;

public class ServiceProviderProfile {

    // Instance Variables
    private String address, phoneNumber, companyName, id;      // String object that stores the address, phone number, company name, and the id of the service provider in the database respectively
    private boolean licensed;                                 // boolean object that is true when the service provider is licensed, false otherwise
    private ArrayList<String> services;                      // String ArrayList containing all the services provided by the service provider
    private ArrayList<String> availability;                 // String ArrayList containing all the availabilities of the service provider

    /**
     * Constructor Method
     *
     * @param id A string containing id of the service provider in the database
     * @param address A string containing the address of the service provider
     * @param phoneNumber A string containing the phone number of the service provider
     * @param companyName A string containing the name of the company the service provider works for
     * @param licensed Boolean that is true if the service provider is licensed, false otherwise
     * @param availability An ArrayList<String> containing the availability of the service provider
     * @param services An ArrayList<String> containing the services offered by the service provider
     */
    public ServiceProviderProfile (String id, String address, String phoneNumber, String companyName, boolean licensed, ArrayList<String> availability, ArrayList<String> services) {

        this.id=id;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.companyName=companyName;
        this.services=services;
        this.availability=availability;
        this.licensed=licensed;
    }

    /**
     * Constructor Method
     */
    public ServiceProviderProfile () {

    }

    // Getter and Setter Methods

    /**
     * Getter method for the address of the service provider
     *
     * @return A string containing the address of the service provider
     */
    public String getAddress() {

        return address;
    }

    /**
     * A getter method for the phone number of the service provider
     *
     * @return A string containing the phone number of the service provider
     */
    public String getPhoneNumber() {

        return phoneNumber;
    }

    /**
     * A getter method for the name of the company the service provider
     * works for
     *
     * @return A String containing the name of the company the service provider works for
     */
    public String getCompanyName() {

        return companyName;
    }

    /**
     * Getter method of the licensed state of the service provider
     *
     * @return true if the service provider is licensed, false otherwise
     */
    public boolean getLicensed() {

        return licensed;
    }

    /**
     * Getter method for the services provided by the service provider
     *
     * @return ArrayList<String> containing the services provided by the service provider
     */
    public ArrayList<String> getServices() {

        return services;
    }

    /**
     * Getter method for the service provider availabilities
     *
     * @return ArrayList<String> containing the availabilities of the service provider
     */
    public ArrayList<String> getAvailability() {

        return availability;
    }

    /**
     * Getter method for the id of the service provider in the database
     *
     * @return A String containing the id of the service provider in the database
     */
    public String getId() {

        return id;
    }
}
