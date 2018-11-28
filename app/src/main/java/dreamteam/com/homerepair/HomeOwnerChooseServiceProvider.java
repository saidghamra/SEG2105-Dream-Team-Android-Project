package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeOwnerChooseServiceProvider extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference database;
    private ArrayList<String> toDisplay;
    private String searchBy, text, homeOwnerID;
    private Button search_Button;
    private EditText search_EditText;
    private Spinner searchBy_Spinner;
    private ListView displaySearchResults_List;
    private String[] searchParameters = {"Type Of Service", "Time", "Rating"};
    private ArrayList<Service> services;
    private ArrayList<Rate> rates;
    private ArrayList<Booking> bookings;
    private ArrayList<ServiceProviderProfile> profiles;
    private TextView view;
    private int t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_choose_service_provider);

        // Initializing Variables
        toDisplay = new ArrayList<>();
        services = new ArrayList<>();
        rates = new ArrayList<>();
        profiles = new ArrayList<>();
        bookings = new ArrayList<>();
        searchBy = "";

        // Getting the home owner databse id from the previous activity
        Intent intent = new Intent();
        homeOwnerID = intent.getStringExtra("HOMEOWNERID");

        // Initializing Buttons, EditTexts, Spinner, and ListView
        search_Button = (Button) findViewById(R.id.search_button);
        search_EditText = (EditText) findViewById(R.id.searchResults_EditText);
        displaySearchResults_List = findViewById(R.id.display_services);
        searchBy_Spinner = (Spinner) findViewById(R.id.searchType_Spinner);
        searchBy_Spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,searchParameters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchBy_Spinner.setAdapter(adapter);
        view = findViewById(R.id.textView2);

        // Gets all the services, rates, service provider profiles, and bookings available in the database upon launch
        getServices();
        getRates();
        getProfiles();
        getBookings();

        // When the search button is clicked
        search_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Getting the user inputted text
                text = search_EditText.getText().toString().trim();

                // If the search EditText is empty
                if (text.equals("")) {

                    Toast.makeText(getApplicationContext(), "Please make sure the search field is not empty! ", Toast.LENGTH_SHORT).show();
                }

                // Making sure the rating is an integer less than or equal to 5 if a home owner selects to search by rating
                else if (searchBy.equals("Rating") && ratingUnvalid()) {

                    // Do nothing since everything is handled in ratingUnvalid()
                }

                // Making sure the time format is entered correctly by the user in the search TextField
                else if (searchBy.equals("Time") && timeUnvalid()) {

                    // Do nothing since everything is handled in timeUnvalid()
                }

                // If all conditions pass
                else {

                    getSearchResults();
                    updateList();
                }
            }
        });

        // When an item in the list containing the service providers is clicked
        displaySearchResults_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TO IMPLEMENT
                showBookServiceProviderDialog();
                showRatingDialog();
                //addBooking(new Booking(homeOwnerID, id , service, t1, t2));
            }
        });
    }

    /**
     * This method checks whether the rating is an integer less than or equal
     * to 5 if a home owner selects to search by rating.
     *
     * @return true if the rating is unvalid, false otherwise
     */
    public boolean ratingUnvalid() {

        // boolean result used to return the result
        boolean result = false;

        // Trying to parse the String text to check if its an integer or not
        try {

            int test = Integer.parseInt(text);

            if (test>5) {

                Toast.makeText(getApplicationContext(), "Please enter an integer rating less than or equal to five!" , Toast.LENGTH_SHORT).show();
                result = true;
            }
        }

        // If the user inputs anything other than an integer in the search field while searching by rating
        catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Please enter an integer less than or equal to five in the search field. Example: 1" , Toast.LENGTH_SHORT).show();
            result=true;
        }

        return result;
    }

    /**
     *  This method checks whether the time entered by the user in the EditText field
     *  matches a 9-2 or 12-9 format.
     *
     * @return true if the time is invalid, false otherwise
     */
    public boolean timeUnvalid() {

        boolean result = false;                // boolean result used to return the result
        String[] timeslots = {"9-11", "11-1", "1-3", "3-5"};
        // Checking if the text inputted by the user contains a -
        if (!text.contains("-")) {

            result = true;
            Toast.makeText(getApplicationContext(), "Please make sure to enter a time in the format 9-12 or 12-9!" , Toast.LENGTH_SHORT).show();
        }

        // Checking if the user entered the correct time format (12-9)
        else {

            try {
                // Checking if the user entered the correct time format (12-9)
                t1 = Integer.parseInt(text.substring(0,text.indexOf("-")));
                t2 = Integer.parseInt(text.substring(text.indexOf("-")+1,text.length()));

                // Checking if the user inputted one of the correct time slots
                if (!ArrayUtils.contains(timeslots, text)) {

                    result = true;
                    Toast.makeText(getApplicationContext(), "Please limit your search to one of the following: 9-11, 11-1, 1-3, 3-5!" , Toast.LENGTH_SHORT).show();
                }
            }

            // If the user inputted something different than an int for the time part
            catch (Exception e) {

                result=true;
                Toast.makeText(getApplicationContext(), "Please make sure to enter a time in the format 9-12 or 12-9!" , Toast.LENGTH_SHORT).show();
            }
        }

        return result;
    }

    /**
     * This method gets all the services stored in the database.
     */
    private void getServices() {

        database = FirebaseDatabase.getInstance().getReference("services");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clearing the ArrayList services
                services.clear();

                // Getting all the services in the database and adding them to the ArrayList adminServices
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Service service = postSnapShot.getValue(Service.class);
                    services.add(service);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting information from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method gets all the rates stored in the database.
     */
    private void getRates() {

        database = FirebaseDatabase.getInstance().getReference("rates");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clearing the ArrayList services
                rates.clear();

                // Getting all the services in the database and adding them to the ArrayList adminServices
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Rate rate = postSnapShot.getValue(Rate.class);
                    rates.add(rate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting information from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method gets all the service provider profiles stored in the database.
     */
    private void getProfiles() {

        database = FirebaseDatabase.getInstance().getReference("serviceproviderprofiles");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clearing the ArrayList services
                profiles.clear();

                // Getting all the services in the database and adding them to the ArrayList adminServices
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    ServiceProviderProfile profile = postSnapShot.getValue(ServiceProviderProfile.class);
                    profiles.add(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting information from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method gets all the bookings from the database.
     */
    private void getBookings() {

        database = FirebaseDatabase.getInstance().getReference("bookings");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clearing the ArrayList services
                bookings.clear();

                // Getting all the services in the database and adding them to the ArrayList adminServices
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Booking booking = postSnapShot.getValue(Booking.class);
                    bookings.add(booking);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting information from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method sets the variable searchBy to the appropriate type based on
     * the item selected in the spinner. It also resets the text field search_EditText
     * after every selection. It also displays a toast informing the user that a
     * certain method of searching is selected.
     *
     * @param arg0
     * @param arg1
     * @param position Position of the item clicked in the spinner
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        // Setting the user type
        searchBy = searchParameters[position];

        // Clearing the ListView and EditText
        toDisplay.clear();
        search_EditText.setText("");
        view.setText("");
        updateList();

        // Displaying toast with the user type selected
        Toast.makeText(getApplicationContext(), "Search by: " + searchBy, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method does nothing when no item is selected on the spinner,
     * since an item is already selected by default.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        // Do nothing
    }

    /**
     * Depending on the type of search the home owner wants to perform, this method
     * goes through the appropriate ArrayList and determines whether a service, rate, or
     * time slot matches the users input. If it does, it is added to the ArrayList toDisplay
     * so it can be displayed in the ListView.
     */
    public void getSearchResults() {

        // Clearing the ArrayList toDisplay
        toDisplay.clear();

        // If the home owner wants to search by type of service
        if (searchBy.equals("Type Of Service")) {

            // Checking if the service exists
            for (int i=0; i<services.size(); i++) {

                Service service = services.get(i);

                // If the service exists, go through every service provider profile and find out who offers it
                if (text.toLowerCase().equals(service.getName().toLowerCase())) {

                    // Getting all the service provider profiles
                    for (int x=0; x<profiles.size(); x++) {

                        ServiceProviderProfile prof = profiles.get(x);
                        ArrayList<String> temp = prof.getServices();

                        // Check the services the service provider offers, if they match add it to the list
                        for (int y=0; y<temp.size(); y++){

                            // Add it to the ArrayList toDisplay to display it to the user
                            if (temp.get(y).equals(service.toString())) {

                                toDisplay.add(prof.getName());
                            }
                        }
                    }
                }
            }
        }

        // If the home owner wants to search by rating
        else if (searchBy.equals("Rating")) {

            // Look through all the rates and see which ones match
            for (int i=0; i<rates.size(); i++) {

                Rate rate = rates.get(i);

                // If the rate matches with what the user inputted
                if (Integer.parseInt(text)==rate.getRate()) {

                    // Look through all the service provider profiles and find the name
                    for (int x=0; x<profiles.size(); x++) {

                        ServiceProviderProfile profile = profiles.get(x);

                        // If the name is found, print the service providers name and all the services he offers
                        if (profile.getId().equals(rate.getServiceProviderId())) {

                            // ArrayList containing all the services the service provider offers
                            ArrayList<String> services = profile.getServices();

                            for(int z=0; z<services.size(); z++) {

                                // Adding the String representation to toDisplay to display it in the list
                                toDisplay.add(profile.getName() + ", " + services.get(z));
                            }
                        }
                    }
                }
            }
        }

        // If the home owner wants to search by time
        else if (searchBy.equals("Time")) {

            // Go through all the service provider profiles
            for (int i=0; i<profiles.size(); i++) {

                // Initializing the current profile
                ServiceProviderProfile profile = profiles.get(i);

                // Initializing the current service provider services
                ArrayList<String> services = profile.getServices();

                // Go through all the services the current service provider offers
                for (int y=0; y<services.size(); y++) {

                    // Go through all the bookings stored in the databased
                    for (int x = 0; x < bookings.size(); x++) {

                        // Initializing the current booking
                        Booking booking = bookings.get(x);

                        // Checking current statement, if true, add the service provider name and service offered during the time specified by the user to the list
                        if (!(t1 == booking.getStartTime() && t2 == booking.getEndTime() && profile.getId().equals(booking.getServiceProviderID()) && booking.getService().equals(services.get(y)))) {

                            toDisplay.add(profile.getName() + ", " + services.get(y));
                        }
                    }
                }
            }
        }

        // If no results were found, update the TextView to display so
        if (toDisplay.size()==0) {

            view.setText("\n No service providers were found matching your search!");
        }

        // If results are found, update the TextView to display so
        else {

            view.setText("\n Service providers that match your search: \n");
        }
    }

    /**
     * This method updates the ListView displaySearchResults_List to display
     * the search results based on the users input.
     */
    public void updateList() {

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,toDisplay);
        displaySearchResults_List.setAdapter(adapter1);
    }

    /**
     * This method is called when the service provider wants to rate his service.
     * The service is then uploaded to the databse.
     *
     * @param rate A rate object containing the rate information
     */
    public void addRate(Rate rate) {

        database = FirebaseDatabase.getInstance().getReference("rates");

        String id = database.push().getKey();

        database.child(id).setValue(rate);

        Toast.makeText(getApplicationContext(), "Rate posted!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is called when the home owner chooses a service and time slot.
     * The booking is then uplaoded to the database
     *
     * @param booking A booking object containing the booking information
     */
    public void addBooking(Booking booking) {

        database = FirebaseDatabase.getInstance().getReference("bookings");

        String id = database.push().getKey();

        database.child(id).setValue(booking);

        Toast.makeText(getApplicationContext(), "Booking created!", Toast.LENGTH_SHORT).show();
    }

    // TO IMPLEMENT
    public void showBookServiceProviderDialog() {

        // XML LAYOUT ALREADY EXISTS CALLED book_service_provider
    }

    // TO IMPLEMENT
    public void showRatingDialog() {

        // XML LAYOUT ALREADY EXISTS CALLED rate_service_provider
    }
}
