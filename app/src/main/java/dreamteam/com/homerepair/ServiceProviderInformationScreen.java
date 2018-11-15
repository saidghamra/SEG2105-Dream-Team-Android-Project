package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ServiceProviderInformationScreen extends AppCompatActivity {

    private boolean licensed;
    private String address,phoneNumber,companyName,id;
    private ArrayList<Service> services;
    private ArrayList<String> availability;
    private TextView info;
    private Button editInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_information_screen);

        Intent intent = getIntent();
        address = intent.getStringExtra("ADDRESS");
        phoneNumber = intent.getStringExtra("PHONENUMBER");
        companyName = intent.getStringExtra("COMPANYNAME");
        licensed = intent.getBooleanExtra("LICENSED", false);
        availability = intent.getStringArrayListExtra("AVAILABILITY");
        services = (ArrayList<Service>) intent.getSerializableExtra("SERVICES");

        updateTextView();

        // Initializing Button
        editInfo = findViewById(R.id.editInformation_button);
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    public void updateTextView() {

        info = findViewById(R.id.serviceProviderInformation);
        info.setText("Address: " + address
                    + "\nPhone Number: " + phoneNumber
                    + "\nCompany Name: " + companyName
                    + "\n Licensed: " + licensed
                    + "\nAvailability: " + availability
                    + "\n Services: " + services);
    }
}
