package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceProviderInformationScreen extends AppCompatActivity {

    private String id;                              // String object that stores the database id of the service provider
    private TextView info;                         // TextView object that is used to display the service providers profile information
    private Button editInfo;                      // Stores a Button object that is used if the service provider wants to update his profile information
    private DatabaseReference database;          // Stores a DatabaseReference object for FireBase use.

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_information_screen);

        // Getting the service provider database id from ServiceProviderProfileSetUp
        Intent intent = getIntent();
        id = intent.getStringExtra("SERVICEPROVIDERDBID");

        // Getting the service providers profile and updating the TextView
        getProfile();

        // Initializing Button
        editInfo = findViewById(R.id.editInformation_button);
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If the service provider wants to edit his profile information
                Intent intent = new Intent(v.getContext(), ServiceProviderProfileSetUp.class);
                intent.putExtra("SERVICEPROVIDERID", id);
                startActivity(intent);
            }
        });

    }

    /**
     * This method updates the TextView with the service provider profile information.
     */
    public void updateTextView(ServiceProviderProfile profile) {

        // Initializing the TextView
        info = findViewById(R.id.serviceProviderInformation);
        info.setText("Address: " + profile.getAddress()
                    + "\nPhone Number: " + profile.getPhoneNumber()
                    + "\nCompany Name: " + profile.getCompanyName()
                    + "\n Licensed: " + profile.getLicensed()
                    + "\nAvailability: " + profile.getAvailability()
                    + "\n Services: " + profile.getServices());
    }

    /**
     * This method accesses the database and retrieves all the service provider profiles.
     * It then calls the method updateTextView() to display the profile information of the
     * current service provider, if it is found.
     */
    private void getProfile() {

        database = FirebaseDatabase.getInstance().getReference("serviceproviderprofiles");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Accessing all the profiles stored in the database
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                   ServiceProviderProfile profile = postSnapShot.getValue(ServiceProviderProfile.class);

                   // When the service provider profile we are looking for is found, update the text view
                   if (profile.getId().equals(id)) {

                       // Updates the TextView to display the service providers profile information
                       updateTextView(profile);
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
}
