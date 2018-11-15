package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private Button completeProfileButton;
    private DatabaseReference database;
    private boolean licensed;
    private String address,phoneNumber,companyName,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile_set_up);

        // By default, the service provider is not licensed
        licensed=false;

        // Getting the Service Provider database id from SignUpActivity
        Intent intent = getIntent();
        id = intent.getStringExtra("SERVICEPROVIDERDBID");

        // Initializing EditText Objects
        address_Text = (EditText) findViewById(R.id.address);
        phoneNumber_Text = (EditText) findViewById(R.id.phoneNumber);
        companyName_Text = (EditText) findViewById(R.id.companyName);

        // Initializing Button
        completeProfileButton = findViewById(R.id.completeProfile_Button);
        completeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                address = address_Text.getText().toString().trim();
                phoneNumber = phoneNumber_Text.getText().toString().trim();
                companyName = companyName_Text.getText().toString().trim();

                if (address.equals("") || phoneNumber.equals("") || companyName.equals("")) {

                    Toast.makeText(getApplicationContext(), "Invalid input. Please make sure none of the fields are empty.", Toast.LENGTH_SHORT).show();
                }

                else{

                    createProfile();
                    Toast.makeText(getApplicationContext(), "Profile completed!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        licensed_switch = findViewById(R.id.licensed_switch);
        licensed_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (licensed_switch.isChecked()) {

                    licensed=true;
                }

                else if (!licensed_switch.isChecked()) {

                    licensed=false;
                }
            }
        });
    }

    private void getServices() {

    }

    private void createProfile() {

        database = FirebaseDatabase.getInstance().getReference("ServiceProviderProfiles");
        String otherID = database.push().getKey();

        // Creating the ServiceProviderProfile object
        ServiceProviderProfile profile = new ServiceProviderProfile(id, address, phoneNumber, companyName, licensed, new ArrayList<Service>());

        database.child(otherID).setValue(profile);
    }
}
