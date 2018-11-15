package dreamteam.com.homerepair;

import android.content.Intent;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ServiceProviderProfileSetUp extends AppCompatActivity {

    private EditText address_Text,phoneNumber_Text,companyName_Text;
    private Switch licensed_switch;
    private Button completeProfileButton, addAvailability, addServices;
    private DatabaseReference database;
    private boolean licensed;
    private String address,phoneNumber,companyName,id;
    private ArrayList<Service> services;
    private ArrayList<String> availability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile_set_up);

        // By default, the service provider is not licensed
        licensed=false;

        //Initializing ArrayLists
        services = new ArrayList<>();
        availability = new ArrayList<>();

        // Getting the Service Provider database id from SignUpActivity
        Intent intent = getIntent();
        id = intent.getStringExtra("SERVICEPROVIDERDBID");

        // Initializing EditText Objects
        address_Text = (EditText) findViewById(R.id.address);
        phoneNumber_Text = (EditText) findViewById(R.id.phoneNumber);
        companyName_Text = (EditText) findViewById(R.id.companyName);

        // Initializing Button
        addAvailability =  findViewById(R.id.add_availability);
        // If the service provider wants to set his availability
        addAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHoursDialog();
            }
        });

        // Initializing Button
        addServices =  findViewById(R.id.add_services);
        // If the service provider wants to set up the services he provides
        addServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showServicesDialog();
            }
        });

        // Initializing Button
        completeProfileButton = findViewById(R.id.completeProfile_Button);
        // If the service provider wants to complete his profile
        completeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting values from the EditTexts
                address = address_Text.getText().toString().trim();
                phoneNumber = phoneNumber_Text.getText().toString().trim();
                companyName = companyName_Text.getText().toString().trim();

                // Field Validations
                // If any of the EditTexts are empty
                if (address.equals("") || phoneNumber.equals("") || companyName.equals("")) {

                    Toast.makeText(getApplicationContext(), "Invalid input. Please make sure none of the fields are empty.", Toast.LENGTH_SHORT).show();
                }

                // If the phone number length is not 10 digits in length
                else if (phoneNumber.length()!=10) {

                    Toast.makeText(getApplicationContext(), "Please make sure the phone number is in the form of 1234567892.", Toast.LENGTH_SHORT).show();
                }
                // If the service provider didnt set any availability
                else if(availability.size()==0) {

                    Toast.makeText(getApplicationContext(), "Please add at least one availability!", Toast.LENGTH_SHORT).show();
                }

                // If the service provier didnt select any services
                else if (services.size()==0) {

                    Toast.makeText(getApplicationContext(), "Please add at least one service!", Toast.LENGTH_SHORT).show();
                }

                // If all checks pass, create the profile and move on to the next screen
                else{

                    createProfile();
                    Toast.makeText(getApplicationContext(), "Profile completed!", Toast.LENGTH_SHORT).show();
                    showInformationScreen();
                }
            }
        });

        // Switch used to determine whether a service provider is licensed or not
        licensed_switch = findViewById(R.id.licensed_switch);
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

    private void getServices() {

    }

    /**
     *
     */
    private void createProfile() {

        database = FirebaseDatabase.getInstance().getReference("ServiceProviderProfiles");
        String otherID = database.push().getKey();

        // Creating the ServiceProviderProfile object
        ServiceProviderProfile profile = new ServiceProviderProfile(id, address, phoneNumber, companyName, licensed, availability, services);

        database.child(otherID).setValue(profile);
    }

    /**
     *
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
     *
     */
    private void showServicesDialog() {

    }

    /**
     *
     */
    private void showInformationScreen() {

        // Creating a new intent
        Intent intent = new Intent(this, ServiceProviderInformationScreen.class);

        // Passing all parameters to ServiceProviderProfile
        intent.putExtra("ADDRESS",address);
        intent.putExtra("PHONENUMBER",phoneNumber);
        intent.putExtra("COMPANYNAME",companyName);
        intent.putExtra("LICENSED",licensed);
        intent.putExtra("AVAILABILITY",availability);
        intent.putExtra("SERVICES",services);

        // Starting the activity
        startActivity(intent);
    }
}
