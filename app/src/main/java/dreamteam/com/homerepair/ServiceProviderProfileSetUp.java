package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceProviderProfileSetUp extends AppCompatActivity {

    private EditText name_Text,address_Text,phoneNumber_Text,companyName_Text;               // Stores EditText objects for the service providers name, address, phone number, and company name
    private Switch licensed_switch;                                                         // Stores Switch object that is used to determine whether the service provider is licensed or not
    private Button completeProfileButton, addAvailability, addServices;                    // Stores Button objects that are used to determine when the service provider wants to complete his profile, add availabilities or services
    private DatabaseReference database;                                                   // Stores a DatabaseReference object for FireBase use.
    private boolean licensed, profileExists;                                             // boolean used to determine whether the service provider is licensed and whether his profile exists
    private String name,address,phoneNumber,companyName,id,profileID;                   // Strings used to store the service providers name, address, phone number, company name, database id, and profile database id
    private ArrayList<String> availability, services;                                  // ArrayLists<String> used to store the service providers availabilities and services

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile_set_up);

        // By default, the service provider is not licensed and the profile of the service provider doesn't exist in the database
        licensed=false;
        profileExists=false;

        //Initializing ArrayLists
        services = new ArrayList<>();
        availability = new ArrayList<>();

        // Getting the Service Provider database id from SignUpActivity
        Intent intent = getIntent();
        id = intent.getStringExtra("SERVICEPROVIDERID");

        // Initializing Buttons and EditTexts
        address_Text = (EditText) findViewById(R.id.address);
        phoneNumber_Text = (EditText) findViewById(R.id.phoneNumber);
        companyName_Text = (EditText) findViewById(R.id.companyName);
        addAvailability =  findViewById(R.id.add_availability);
        addServices =  findViewById(R.id.add_services);
        completeProfileButton = findViewById(R.id.completeProfile_Button);
        licensed_switch = findViewById(R.id.licensed_switch);
        name_Text =  findViewById(R.id.name);

        // Method is called to check if the profile exists
        profileExists();

        // If the service provider wants to set his availability
        addAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHoursDialog();
            }
        });

        // If the service provider wants to set up the services he provides
        addServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showServicesScreen();
            }
        });

        // If the service provider wants to complete his profile
        completeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting values from the EditTexts
                address = address_Text.getText().toString().trim();
                phoneNumber = phoneNumber_Text.getText().toString().trim();
                companyName = companyName_Text.getText().toString().trim();
                name = name_Text.getText().toString().trim();

                // Field Validations
                // If any of the EditTexts are empty
                if (address.equals("") || phoneNumber.equals("") || companyName.equals("") || name.equals("")) {

                    Toast.makeText(getApplicationContext(), "Invalid input. Please make sure none of the fields are empty.", Toast.LENGTH_SHORT).show();
                }

                // If the phone number length is not 10 digits in length
                else if (phoneNumber.length()!=10) {

                    Toast.makeText(getApplicationContext(), "Please make sure the phone number is in the form of 1234567892.", Toast.LENGTH_SHORT).show();
                }
                // If the service provider didn't set any availability
                else if(availability.size()==0) {

                    Toast.makeText(getApplicationContext(), "Please add at least one availability!", Toast.LENGTH_SHORT).show();
                }

                // If the service provider didn't select any services
                else if (services.size()==0) {

                    Toast.makeText(getApplicationContext(), "Please add at least one service!", Toast.LENGTH_SHORT).show();
                }

                // If all checks pass, create the profile and move on to the next screen
                else{

                    createProfile();
                    showInformationScreen();
                }
            }
        });

        // Switch used to determine whether a service provider is licensed or not
        licensed_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If the service provider is unlicensed
                if (licensed_switch.isChecked()) {

                    licensed=true;
                }

                // If the service provider is licensed
                else if (!licensed_switch.isChecked()) {

                    licensed=false;
                }
            }
        });
    }

    /**
     * This method is called when all the validations pass and creates the ServiceProviderProfile
     * object and adds it to the database. If the profile already exists in the database, it over-
     * writes it.
     */
    private void createProfile() {

        // If the profile exists in the database, update it
        if (profileExists) {

            // Getting the specified service provider profile reference
            database = FirebaseDatabase.getInstance().getReference("serviceproviderprofiles").child(profileID);

            // Updating the service provider profile
            ServiceProviderProfile profile = new ServiceProviderProfile(id, name, address, phoneNumber, companyName, licensed, availability, services);
            database.setValue(profile);

            Toast.makeText(getApplicationContext(), "Updated Profile!", Toast.LENGTH_SHORT).show();
        }

        // If the profile doesn't exist in the database, create it
        else {

            database = FirebaseDatabase.getInstance().getReference("serviceproviderprofiles");

            String otherID = database.push().getKey();

            // Creating the ServiceProviderProfile object
            ServiceProviderProfile profile = new ServiceProviderProfile(id, name, address, phoneNumber, companyName, licensed, availability, services);

            database.child(otherID).setValue(profile);

            Toast.makeText(getApplicationContext(), "Created Profile!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method shows the dialog containing the days and hours a service provider
     * can choose from to provide his services. This method is called whenever
     * the service provider clicks on the add availability button.
     */
    private void showHoursDialog() {

        // Building the AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.hours_dialog, null);
        dialogBuilder.setView(dialogView);

        // Initializing the switches
        final Switch monday = (Switch) dialogView.findViewById(R.id.monday_switch);
        final Switch tuesday = (Switch) dialogView.findViewById(R.id.tuesday_switch);
        final Switch wednesday = (Switch) dialogView.findViewById(R.id.wednesday_switch);
        final Switch thursday = (Switch) dialogView.findViewById(R.id.thursday_switch);
        final Switch friday = (Switch) dialogView.findViewById(R.id.friday_switch);
        final Switch saturday = (Switch) dialogView.findViewById(R.id.saturday_switch);
        final Switch sunday = (Switch) dialogView.findViewById(R.id.sunday_switch);

        // If availabilities already exists, update the switches to reflect so
        if (availability.size()!=0) {

            for (int i=0; i<availability.size(); i++) {

                if (availability.get(i).equals("Monday 9-5")) {
                    monday.setChecked(true);
                }
                if (availability.get(i).equals("Tuesday 9-5")) {

                    tuesday.setChecked(true);
                }
                if (availability.get(i).equals("Wednesday 9-5")) {
                    wednesday.setChecked(true);
                }
                if (availability.get(i).equals("Thursday 9-5")) {
                    thursday.setChecked(true);
                }
                if (availability.get(i).equals("Friday 9-5")) {
                    friday.setChecked(true);
                }
                if (availability.get(i).equals("Saturday 9-5")) {
                    saturday.setChecked(true);
                }
                if (availability.get(i).equals("Sunday 9-5")) {
                    sunday.setChecked(true);
                }
            }
        }

        // Showing the dialog
        dialogBuilder.setTitle("Availability");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // When the service provider is done selecting his availability, gather all availabilities
        final Button done = (Button) dialogView.findViewById(R.id.availabilityDone_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Clearing the ArrayList availability
                availability.clear();

                // Determining what days the service provider wants to work on and adding them to the ArrayList availability
                if (monday.isChecked()) {
                    availability.add("Monday 9-5");
                }
                if (tuesday.isChecked()) {
                    availability.add("Tuesday 9-5");
                }
                if (wednesday.isChecked()) {
                    availability.add("Wednesday 9-5");
                }
                if (thursday.isChecked()) {
                    availability.add("Thursday 9-5");
                }
                if (friday.isChecked()) {
                    availability.add("Friday 9-5");
                }
                if (saturday.isChecked()) {
                    availability.add("Saturday 9-5");
                }
                if (sunday.isChecked()) {
                    availability.add("Sunday 9-5");
                }

                // Dismissing the dialog
                b.dismiss();
            }
        });
    }

    /**
     * This method shows a screen that contains all the services the admin offers
     * so the service provider can choose from. This method is called whenever
     * the service provider clicks on the add services button.
     */
    private void showServicesScreen() {

        // Launching a new intent to get the services the service provider chose
        Intent intent = new Intent(this, chooseServices.class);
        intent.putExtra("SERVICES",services);
        startActivityForResult(intent,5);
    }

    /**
     * This method is called when the service provider completes his profile
     * and moves on to ServiceProviderInformationScreen screen where the service
     * provider can see the information he just entered.
     */
    private void showInformationScreen() {

        // Creating a new intent
        Intent intent = new Intent(this, ServiceProviderInformationScreen.class);

        // Passing the database id of the service provider to ServiceProviderInformationScreen
        intent.putExtra("SERVICEPROVIDERDBID",id);

        // Starting the activity
        startActivity(intent);
    }

    /**
     * Overriding the method onActivityResult inorder to get the list of services
     * that the service provider chose.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {

            services = data.getStringArrayListExtra("SERVICESCHOSEN");
        }
    }

    /**
     * This method retrieves all the service provider profiles from the database and checks whether
     * the service provider id stored in this activity is associated with a service provider profile.
     * If so, call method populateFields(). This is used when the service provider wants to edit his
     * profile information.
     */
    public void profileExists() {

        database = FirebaseDatabase.getInstance().getReference("serviceproviderprofiles");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Accessing all the profiles stored in the database
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    ServiceProviderProfile profile = postSnapShot.getValue(ServiceProviderProfile.class);

                    // When the service provider profile we are looking for is found, update the text view
                    if (profile.getId().equals(id)) {

                        profileID = postSnapShot.getKey();
                        populateFields(profile);
                        profileExists=true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting the service provider profile from the database!" + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method populates all the fields with information from the service provider
     * profile stored in the database. Only called when the service provider profile
     * already exists in the database.
     *
     * @param profile The service providers profile
     */
    public void populateFields(ServiceProviderProfile profile) {

        address_Text.setText(profile.getAddress());
        phoneNumber_Text.setText(profile.getPhoneNumber());
        companyName_Text.setText(profile.getCompanyName());
        services=profile.getServices();
        availability=profile.getAvailability();
        licensed=profile.getLicensed();
        licensed_switch.setChecked(licensed);
        name_Text.setText(profile.getName());
    }
}
