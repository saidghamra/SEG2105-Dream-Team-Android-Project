package dreamteam.com.homerepair;

import java.util.ArrayList;

public class ServiceProviderProfile {

    // Instance Variables
    private String address, phoneNumber, companyName, id;
    private boolean licensed;
    private ArrayList<Service> services;

    // Constructor Methods
    public ServiceProviderProfile (String id, String address, String phoneNumber, String companyName, boolean licensed, ArrayList<Service> services) {
        this.id=id;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.companyName=companyName;
        this.services=services;
        this.licensed=licensed;
    }

    public ServiceProviderProfile () {

    }

    // Getter and Setter Methods
    public String getAddress() {

        return address;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public String getCompanyName() {

        return companyName;
    }

    public boolean getLicensed() {

        return licensed;
    }

    public ArrayList<Service> getServices() {

        return services;
    }

    public void addService (Service service) {

        services.add(service);
    }

    public void removeService(Service service) {

        services.remove(service);
    }
}
